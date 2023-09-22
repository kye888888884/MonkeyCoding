from django.db import models

# Create your models here.
class Answer(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=100)
    answer = models.IntegerField()

    def __str__(self):
        return self.name