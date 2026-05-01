namespace PlantGardeningGuide.Models
{
    public class PlantSubmission
    {
        public int PlantSubmissionID { get; set; }
        public int UserID { get; set; }
        public string PlantName { get; set; } = string.Empty;
        public string PlantType { get; set; } = string.Empty;
        public string Sunlight { get; set; } = string.Empty;
        public string WaterNeeds { get; set; } = string.Empty;
        public string Difficulty { get; set; } = string.Empty;
        public string CareTips { get; set; } = string.Empty;
        public bool IsIndoor { get; set; }
        public bool IsPoisonous { get; set; }
        public string Notes { get; set; } = string.Empty;
        public string Status { get; set; } = string.Empty;
        public DateTime SubmittedOn { get; set; }
        public User? User { get; set; }
    }
}
