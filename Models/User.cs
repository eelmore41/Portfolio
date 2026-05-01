namespace PlantGardeningGuide.Models
{
    public class User
    {
        public int UserID { get; set; }
        public string Username { get; set; } = string.Empty;
        public string Password { get; set; } = string.Empty;
        public ICollection<UserPlant> UserPlants { get; set; } = new List<UserPlant>();
        public ICollection<Review> Reviews { get; set; } = new List<Review>();
    }
}
