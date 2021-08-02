from datetime import timezone
from django.db import models
from django.contrib.auth.models import User
# from django.db.models.signals import post_save
# from django.dispatch import receiver

# 유저 모델 - 아이디, 비밀번호, 닉네임

class sdiUser(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    user_subjects = models.CharField(max_length=128, null=True)             # textfield로 받은 후 json에서 리스트로 처리하고 할 예정

    def __str__(self):
        return 'sdiUser: {}'.format(self.user)


# Board Model - 게시글 - 과목 코드, 글 제목, 글 내용
class Post(models.Model):
    author = models.ForeignKey(sdiUser, on_delete=models.CASCADE, null=True)
    subject = models.CharField(max_length=10, null=False)
    title = models.CharField(max_length=128, null=False)
    context = models.TextField(null=False)
    created = models.DateTimeField(auto_now_add=True, null=True)   # 저장 즉시 해당 날짜와 시간 저장

    def __str__(self):
        return 'Post: {}'.format(self.title)

class Comment(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE, db_column="post")
    content = models.TextField(help_text="Comment contents", blank=False, null=False)
    created = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return 'Comment: {}'.format(self.post)

# To-Do-List Model - 미션 적기
class ToDoList(models.Model):
    subject_code = models.CharField(max_length=10, null=False)
    context = models.CharField(max_length=128, null=False)
    checked = models.BooleanField(default=False)

    def __str__(self):
        return 'ToDoList: {}'.format(self.subject_code)
