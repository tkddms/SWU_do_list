from django.contrib import admin
from .models import sdiUser, Post, ToDoList

admin.site.register(sdiUser)
admin.site.register(Post)
admin.site.register(ToDoList)

# Register your models here.
