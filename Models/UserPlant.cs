namespace PlantGardeningGuide.Models
{
    public class UserPlant
    {
        public int ID { get; set; }
        public int UserID { get; set; }
        public int PlantID { get; set; }
        public string Status { get; set; } = string.Empty;
        public bool IsFavorite { get; set; }
        public string CustomCareTips { get; set; } = string.Empty;
        public string ImagePath { get; set; } = string.Empty;
        public User? User { get; set; }
        public Plant? Plant { get; set; }
    }
}
