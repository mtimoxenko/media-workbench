resource "aws_instance" "web" {
  ami           = "ami-0fc5d935ebf8bc3bc" # Ubuntu Server 22.04 LTS
  instance_type = "t2.micro"
  key_name      = "workbench" # assigned key pair at launch

  tags = {
    Name = "workbench-app"
  }
}

output "ec2_public_ip" {
  description = "Public IP of the EC2 instance"
  value       = aws_instance.web.public_ip
}
