import json
import re
from django.contrib import auth
from django.contrib.auth import authenticate
from django.contrib.auth.decorators import login_required
from django.contrib.auth.models import User
from django.db.models import F
from django.http import JsonResponse, HttpResponse, QueryDict
# from django.shortcuts import render
from django.utils import timezone
from django.views.decorators.csrf import csrf_exempt
from rest_framework.parsers import JSONParser
from .models import sdiUser, ToDoList, Post
from .serializers import UserSerializer, UserSubjectSerializer
from collections import OrderedDict


# Create your views here.

@csrf_exempt
def users_list(request):
   #  Users 리스트 조회
   if request.method == 'GET':
       query_set = User.objects.all()
       serializer = UserSerializer(query_set, many=True)
       return JsonResponse(serializer.data, safe=False)
   # Users 객체 생성
   elif request.method == 'POST':
       data = JSONParser().parse(request)
       serializer = UserSerializer(data=data)
       if serializer.is_valid():
           serializer.save()
           return JsonResponse(serializer.data, status=201)
       return JsonResponse(serializer.data, status=400)

@csrf_exempt
# 유저 객체 수정, 삭제, 조회
def user(request, pk):
    obj = User.objects.get(pk=pk)

    if request.method == 'GET':
        serializer = UserSerializer(obj)
        return JsonResponse(serializer.data, safe=False)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = UserSerializer(obj, data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        obj.delete()
        return HttpResponse(status=204)

@csrf_exempt
# 로그인 - 앱
def app_login(request):
    if request.method == 'POST':
        print("리퀘스트 로그: " + str(request.body))
        id = request.POST.get('user_id', '')
        pw = request.POST.get('user_pw', '')
        print("id = " + id + " pw = " + pw)

        result = authenticate(username=id, password=pw)

        print(result)

        if result:
            auth.login(request, result)
            print("success login " + str(request.body))
            return JsonResponse({'code': '0000', 'msg': 'login success!'}, status=200)
        else:
            print("fail")
            # status=400 으로 하면 안드로이드 상에서 배제해버려서 결과가 나오지 않음.
            return JsonResponse({'code': '1001', 'msg': 'login failed'}, status=200)

@csrf_exempt
def app_register(request):
    if request.method == 'POST':
        print("리퀘스트 로그(sign_up): " + str(request.body))
        id = request.POST.get('user_r_id', '')
        pw = request.POST.get('user_r_pw', '')
        email = request.POST.get('user_r_email', '')
        subject = request.POST.get('user_tot_subject', '')

        # id 중복 확인
        try:
            User.objects.get(username=id)
            return JsonResponse({'code': '1001', 'msg': 'already created id'}, status=200)
        except:  # 조회 결과가 없다. 등록되지 않은 email - ??
            pass

        user = User.objects.create_user(username=id, email=email, password=pw)

        sdiuser = sdiUser(user=user, user_subjects=subject)
        # users_m = sdiUser.objects.create_user(username=id, user_subjects=subject)
        sdiuser.save()

        result = authenticate(username=id, password=pw)

        if result:
            print("successed signin")
            auth.login(request, user)
            return JsonResponse({'code': '0000', 'msg': 'signin success!'}, status=200)
        else:
            # status=400 으로 하면 안드로이드 상에서 배제해버려서 결과가 나오지 않음.
            return JsonResponse({'code': '1001', 'msg': 'signin failed'}, status=200)

@csrf_exempt
def app_update_subject(request):
    if request.method == 'PUT':
        put = QueryDict(request.body)
        id = put.get("user_update_id")
        subject = put.get("user_update_subject")
        try:
            user = sdiUser(user=User.objects.get(username=id))
            user.user_subjects = subject
            print("subject 확인: " + user.user_subjects)
            return JsonResponse({'code': '0000', 'msg': 'update success!'}, status=200)
        except Exception as e:
            # 해당 id의 User 객체를 불러오지 못했을 때
            print(e)
            return JsonResponse({'code': '1001', 'msg': 'not exist user'}, status=200)

@csrf_exempt
def app_logout(request):
    auth.logout(request)
    return JsonResponse({'code': '0000', 'msg': 'logout success'}, status=200)

# user 정보 얻기
def app_get_user(request):
    put = str(request).split("=")[1]
    id = put.split("'")[0]

    sdiuser = sdiUser.objects.get(user=User.objects.get(username=id))

    email = sdiuser.user.email
    subjcets = sdiuser.user_subjects

    return JsonResponse({'subjects': subjcets, 'email': email}, status=200)

# to-do-list 내용 얻기
def app_get_toDoList(request):
    query = str(request).split("=")[1]
    subject_code = query.split("'")[0]

    # lists = list(ToDoList.objects.filter(subject_code=subject_code).values('checked', 'context'))
    lists = ToDoList.objects.filter(subject_code=subject_code).values('checked', 'context')

    return JsonResponse(list(lists), safe=False, status=202)

# to-do-list 추가
@csrf_exempt
def app_add_toDoList(request):
    if request.method == 'POST':
        subject = request.POST.get("subject_code")
        context = request.POST.get("context")
        print("subject - " + subject + " context - " + context)

        try:
            todolist = ToDoList(subject_code=subject, context=context)
            todolist.save()
            print("todolist 확인: " + todolist.context)
            return JsonResponse({'code': '0000', 'msg': 'add success!'}, status=200)
        except:
            return JsonResponse({'code': '1001', 'msg': 'add fail'}, status=200)

@csrf_exempt
def app_edit_post(request):

    if request.method == 'POST':
        author = request.POST.get("author")
        title = request.POST.get("title")
        context = request.POST.get("context")
        subject = request.POST.get("subject")
        created = timezone.localtime()
        print("subject - " + subject + " context - " + context + " title - " + title + " author - " + author + " create - " + str(created) )

        try:
            post = Post(author=sdiUser.objects.get(user=User.objects.get(username=author)), subject=subject, title=title, context=context, created=created)
            post.save()

            strCreated = created.strftime("%Y-%m-%d %H:%M:%S")
            print("post 확인: " + strCreated)

            return JsonResponse({'created': strCreated}, status=200)

        except:
            return JsonResponse({'created': 'fail'}, status=200)

def app_get_posts(request):
    query = str(request).split("=")[1]
    subject_code = query.split("'")[0]

    lists = Post.objects.filter(subject=subject_code)

    new_list = []

    for list in lists:
        file_data = {
            'author': list.author.user.username,
            'subject': list.subject,
            'title': list.title,
            'context': list.context,
            'created': list.created.strftime("%Y-%m-%d %H:%M:%S")
        }
        new_list.append(file_data)

    return JsonResponse(new_list, safe=False, status=200)
