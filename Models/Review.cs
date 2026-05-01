namespace PlantGardeningGuide.Models
{
    public class Review
    {
        public int ReviewID { get; set; }
        public int UserID { get; set; }
        public int PlantID { get; set; }
        public int ResilienceRating { get; set; }
        public int CareRequirementsRating { get; set; }
        public int LightAdaptabilityRating { get; set; }
        public int GrowthRateRating { get; set; }
        public int SensitivityRating { get; set; }
        public string Comment { get; set; } = string.Empty;
        public User? User { get; set; }
        public Plant? Plant { get; set; }
    }
}
