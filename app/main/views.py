from django.shortcuts import render
from django.http import HttpResponse
from main.models import Answer
from django.views.decorators.csrf import csrf_exempt

# Create your views here.
def index(request):
    if request.method == 'GET':
        name = request.GET.get('name')
        # 쿼리 검색
        answers = Answer.objects.filter(name=name)
        answer = answers[0]
        print(answer.answer)

        return HttpResponse(answer.answer)
    return render(request, 'main/index.html')

@csrf_exempt
def update(request):
    if request.method == 'GET':
        name = request.GET.get('name')
        answer = request.GET.get('answer')
        print(name, answer)

        # 기존에 있던 데이터 삭제
        try:
            answers = Answer.objects.filter(name=name)
            answers.delete()
        except:
            pass

        # DB에 저장
        answer = Answer(name=name, answer=answer)
        answer.save()

        return HttpResponse('success')
    elif request.method == 'POST':
        img = request.FILES.get('file')
        print(img)
        return HttpResponse('post')