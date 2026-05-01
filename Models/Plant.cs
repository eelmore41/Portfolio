namespace PlantGardeningGuide.Models
{
    public class Plant
    {
        public int PlantID { get; set; }
        public string Name { get; set; } = string.Empty;
        public string Type { get; set; } = string.Empty;
        public string Sunlight { get; set; } = string.Empty;
        public string WaterNeeds { get; set; } = string.Empty;
        public string Difficulty { get; set; } = string.Empty;
        public string CareTips { get; set; } = string.Empty;
        public string? ImagePath { get; set; }
        public bool IsIndoor { get; set; }
        public bool IsPoisonous { get; set; }
        public string? WateringTips { get; set; }
        public string? SunlightTips { get; set; }
        public string? SoilTips { get; set; }
        public string? PotDrainageTips { get; set; }
        public string? HumidityTips { get; set; }
        public string? TemperatureTips { get; set; }
        public string? FertilizerTips { get; set; }
        public string? GrowthPruningTips { get; set; }
        public string? PestPreventionTips { get; set; }
        public string? SeasonalCareTips { get; set; }
        public string? StressSignsTips { get; set; }
        public string? PlacementTips { get; set; }
        public string? PropagationTips { get; set; }
        public string? PoisonousParts { get; set; }
        public bool? IsEdible { get; set; }
        public string? HarvestTips { get; set; }
        public ICollection<UserPlant> UserPlants { get; set; } = new List<UserPlant>();
        public ICollection<Review> Reviews { get; set; } = new List<Review>();
    }
}
