using Microsoft.EntityFrameworkCore;
using PlantGardeningGuide.Data;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddRazorPages();
builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromHours(2);
    options.Cookie.HttpOnly = true;
    options.Cookie.IsEssential = true;
});

builder.Services.AddDbContext<PlantDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseSession();

app.Use(async (context, next) =>
{
    var path = context.Request.Path.Value?.ToLowerInvariant() ?? string.Empty;
    var allowedPaths = new[] { "/", "/index", "/login", "/register" };
    var isAllowed = allowedPaths.Contains(path);

    if (!isAllowed && !context.Session.GetInt32("UserID").HasValue)
    {
        context.Response.Redirect("/Login");
        return;
    }

    await next();
});

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<PlantDbContext>();
    db.Database.EnsureCreated();
    RepairDatabase(db);
}

app.MapRazorPages();
app.Run();

void RepairDatabase(PlantDbContext db)
{
    // Keeps database tables usable with the current pages.
    var sqlBatches = new[]
    {
        """
        IF OBJECT_ID(N'dbo.Users', N'U') IS NOT NULL
        AND (
            COL_LENGTH('Users', 'UserID') IS NULL OR
            COL_LENGTH('Users', 'Username') IS NULL OR
            COL_LENGTH('Users', 'Password') IS NULL
        )
        BEGIN
            DROP TABLE Users;
        END
        """,
        """
        IF OBJECT_ID(N'dbo.Users', N'U') IS NULL
        BEGIN
            CREATE TABLE Users (
                UserID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                Username NVARCHAR(450) NOT NULL,
                Password NVARCHAR(MAX) NOT NULL
            );
        END
        """,
        """
        IF OBJECT_ID(N'dbo.Users', N'U') IS NOT NULL
        BEGIN
            IF COL_LENGTH('Users', 'Username') IS NULL ALTER TABLE Users ADD Username NVARCHAR(450) NOT NULL DEFAULT '';
            IF COL_LENGTH('Users', 'Password') IS NULL ALTER TABLE Users ADD Password NVARCHAR(MAX) NOT NULL DEFAULT '';
        END
        """,
        """
        IF OBJECT_ID(N'dbo.UserPlants', N'U') IS NOT NULL
        AND (
            COL_LENGTH('UserPlants', 'UserID') IS NULL OR
            COL_LENGTH('UserPlants', 'PlantID') IS NULL OR
            COL_LENGTH('UserPlants', 'Status') IS NULL OR
            COL_LENGTH('UserPlants', 'IsFavorite') IS NULL OR
            COL_LENGTH('UserPlants', 'CustomCareTips') IS NULL OR
            COL_LENGTH('UserPlants', 'ImagePath') IS NULL
        )
        BEGIN
            DROP TABLE UserPlants;
        END
        """,
        """
        IF OBJECT_ID(N'dbo.UserPlants', N'U') IS NULL
        BEGIN
            CREATE TABLE UserPlants (
                ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                UserID INT NOT NULL,
                PlantID INT NOT NULL,
                Status NVARCHAR(MAX) NOT NULL DEFAULT '',
                IsFavorite BIT NOT NULL DEFAULT 0,
                CustomCareTips NVARCHAR(MAX) NOT NULL DEFAULT '',
                ImagePath NVARCHAR(MAX) NOT NULL DEFAULT ''
            );
        END
        """,
        """
        IF OBJECT_ID(N'dbo.UserPlants', N'U') IS NOT NULL
        BEGIN
            IF COL_LENGTH('UserPlants', 'UserID') IS NULL ALTER TABLE UserPlants ADD UserID INT NOT NULL DEFAULT 0;
            IF COL_LENGTH('UserPlants', 'PlantID') IS NULL ALTER TABLE UserPlants ADD PlantID INT NOT NULL DEFAULT 0;
            IF COL_LENGTH('UserPlants', 'Status') IS NULL ALTER TABLE UserPlants ADD Status NVARCHAR(MAX) NOT NULL DEFAULT '';
            IF COL_LENGTH('UserPlants', 'IsFavorite') IS NULL ALTER TABLE UserPlants ADD IsFavorite BIT NOT NULL DEFAULT 0;
            IF COL_LENGTH('UserPlants', 'CustomCareTips') IS NULL ALTER TABLE UserPlants ADD CustomCareTips NVARCHAR(MAX) NOT NULL DEFAULT '';
            IF COL_LENGTH('UserPlants', 'ImagePath') IS NULL ALTER TABLE UserPlants ADD ImagePath NVARCHAR(MAX) NOT NULL DEFAULT '';
        END
        """,
        """
        IF OBJECT_ID(N'dbo.Reviews', N'U') IS NULL
        BEGIN
            CREATE TABLE Reviews (
                ReviewID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
                UserID INT NOT NULL,
                PlantID INT NOT NULL,
                ResilienceRating INT NOT NULL DEFAULT 3,
                CareRequirementsRating INT NOT NULL DEFAULT 3,
                LightAdaptabilityRating INT NOT NULL DEFAULT 3,
                GrowthRateRating INT NOT NULL DEFAULT 3,
                SensitivityRating INT NOT NULL DEFAULT 3,
                Comment NVARCHAR(MAX) NOT NULL DEFAULT ''
            );
        END
        """,
        """
        IF OBJECT_ID(N'dbo.Reviews', N'U') IS NOT NULL
        BEGIN
            IF COL_LENGTH('Reviews', 'ResilienceRating') IS NULL ALTER TABLE Reviews ADD ResilienceRating INT NOT NULL CONSTRAINT DF_Reviews_ResilienceRating DEFAULT 3;
            IF COL_LENGTH('Reviews', 'CareRequirementsRating') IS NULL ALTER TABLE Reviews ADD CareRequirementsRating INT NOT NULL CONSTRAINT DF_Reviews_CareRequirementsRating DEFAULT 3;
            IF COL_LENGTH('Reviews', 'LightAdaptabilityRating') IS NULL ALTER TABLE Reviews ADD LightAdaptabilityRating INT NOT NULL CONSTRAINT DF_Reviews_LightAdaptabilityRating DEFAULT 3;
            IF COL_LENGTH('Reviews', 'GrowthRateRating') IS NULL ALTER TABLE Reviews ADD GrowthRateRating INT NOT NULL CONSTRAINT DF_Reviews_GrowthRateRating DEFAULT 3;
            IF COL_LENGTH('Reviews', 'SensitivityRating') IS NULL ALTER TABLE Reviews ADD SensitivityRating INT NOT NULL CONSTRAINT DF_Reviews_SensitivityRating DEFAULT 3;
        END
        """,
        """
        IF OBJECT_ID(N'dbo.Plants', N'U') IS NOT NULL
        BEGIN
            IF COL_LENGTH('Plants', 'ImagePath') IS NULL ALTER TABLE Plants ADD ImagePath NVARCHAR(255) NULL;
            IF COL_LENGTH('Plants', 'CareTips') IS NULL ALTER TABLE Plants ADD CareTips NVARCHAR(MAX) NOT NULL DEFAULT '';
            IF COL_LENGTH('Plants', 'WateringTips') IS NULL ALTER TABLE Plants ADD WateringTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'SunlightTips') IS NULL ALTER TABLE Plants ADD SunlightTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'SoilTips') IS NULL ALTER TABLE Plants ADD SoilTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'PotDrainageTips') IS NULL ALTER TABLE Plants ADD PotDrainageTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'HumidityTips') IS NULL ALTER TABLE Plants ADD HumidityTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'TemperatureTips') IS NULL ALTER TABLE Plants ADD TemperatureTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'FertilizerTips') IS NULL ALTER TABLE Plants ADD FertilizerTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'GrowthPruningTips') IS NULL ALTER TABLE Plants ADD GrowthPruningTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'PestPreventionTips') IS NULL ALTER TABLE Plants ADD PestPreventionTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'SeasonalCareTips') IS NULL ALTER TABLE Plants ADD SeasonalCareTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'StressSignsTips') IS NULL ALTER TABLE Plants ADD StressSignsTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'PlacementTips') IS NULL ALTER TABLE Plants ADD PlacementTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'PropagationTips') IS NULL ALTER TABLE Plants ADD PropagationTips NVARCHAR(MAX) NULL;
            IF COL_LENGTH('Plants', 'PoisonousParts') IS NULL ALTER TABLE Plants ADD PoisonousParts NVARCHAR(255) NULL;
            IF COL_LENGTH('Plants', 'IsEdible') IS NULL ALTER TABLE Plants ADD IsEdible BIT NULL;
            IF COL_LENGTH('Plants', 'HarvestTips') IS NULL ALTER TABLE Plants ADD HarvestTips NVARCHAR(MAX) NULL;
        END
        """,
        """
        IF OBJECT_ID(N'dbo.PlantSubmissions', N'U') IS NULL
        BEGIN
            CREATE TABLE PlantSubmissions (
                PlantSubmissionID INT IDENTITY(1,1) PRIMARY KEY,
                UserID INT NOT NULL,
                PlantName NVARCHAR(100) NOT NULL,
                PlantType NVARCHAR(100) NOT NULL,
                Sunlight NVARCHAR(100) NOT NULL,
                WaterNeeds NVARCHAR(100) NOT NULL,
                Difficulty NVARCHAR(100) NOT NULL,
                CareTips NVARCHAR(MAX) NOT NULL,
                IsIndoor BIT NOT NULL,
                IsPoisonous BIT NOT NULL,
                Notes NVARCHAR(MAX) NOT NULL,
                Status NVARCHAR(50) NOT NULL,
                SubmittedOn DATETIME2 NOT NULL
            );
        END
        """
    };

    foreach (var sql in sqlBatches)
    {
        db.Database.ExecuteSqlRaw(sql);
    }
}
