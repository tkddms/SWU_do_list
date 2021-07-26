from django.db import models

# Create your models here.

# 유저 모델 - 아이디, 비밀번호, 닉네임
class Users(models.Model):
    user_name = models.CharField(max_length=10, unique=True)
    user_id = models.CharField(max_length=32, unique=True)
    user_pw = models.CharField(max_length=128)
    user_created = models.DateTimeField(auto_now_add=True)
    user_subjects = models.TextField(null=True)             # textfield로 받은 후 json에서 리스트로 처리하고 할 예정

    def __str__(self):
        return self.user_name

    class Meta:
        ordering = ['user_created']

