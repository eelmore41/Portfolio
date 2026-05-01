using Microsoft.EntityFrameworkCore;
using PlantGardeningGuide.Models;

namespace PlantGardeningGuide.Data
{
    public class PlantDbContext : DbContext
    {
        public PlantDbContext(DbContextOptions<PlantDbContext> options)
            : base(options)
        {
        }

        public DbSet<User> Users { get; set; }
        public DbSet<Plant> Plants { get; set; }
        public DbSet<UserPlant> UserPlants { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<PlantSubmission> PlantSubmissions { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Plant>()
                .HasKey(p => p.PlantID);

            modelBuilder.Entity<Plant>()
                .Property(p => p.PlantID)
                .HasColumnName("ID");

            modelBuilder.Entity<User>()
                .HasIndex(u => u.Username)
                .IsUnique();

            modelBuilder.Entity<UserPlant>()
                .HasIndex(up => new { up.UserID, up.PlantID })
                .IsUnique();
        }
    }
}
