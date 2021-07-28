from django.db import models
from django.contrib.auth.models import User


# Create your models here.

# 유저 모델 - 아이디, 비밀번호, 닉네임
class sdiUser(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    user_subjects = models.TextField(null=True)             # textfield로 받은 후 json에서 리스트로 처리하고 할 예정
    def __str__(self):
        return self.username

# Board Model - 게시글 - 과목 코드, 글 제목, 글 내용
class Post(models.Model):
    subject_code = models.CharField(max_length=10, null=False)
    title = models.CharField(max_length=128, null=False)
    context = models.TextField(null=False)


# To-Do-List Model - 미션 적기
class ToDoList(models.Model):
    subject_code = models.CharField(max_length=10, null=False)
    context = models.CharField(max_length=128, null=False)
    checked = models.BooleanField(default=False)