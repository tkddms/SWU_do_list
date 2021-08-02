from rest_framework import serializers
from .models import sdiUser, Post, Comment


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = sdiUser
        fields = ['username', 'password', 'email']

class UserSubjectSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = sdiUser
        fields = ['username', 'user_subjects']

class PostSerializer(serializers.ModelSerializer):
    class Meta:
        model = Post
        fields = ['subject', 'author', 'title', 'content', 'created']

class CommentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Comment
        fields = ['title', 'author', 'content', 'created']
