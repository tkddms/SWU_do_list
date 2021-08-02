from django.contrib import admin
from .models import sdiUser, Post, ToDoList, Comment

admin.site.register(sdiUser)
admin.site.register(Post)
admin.site.register(ToDoList)
admin.site.register(Comment)

# Register your models here.
