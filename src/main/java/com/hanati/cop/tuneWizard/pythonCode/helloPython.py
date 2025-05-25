#print('hello python chanju')

import sys
import json

# JSON 문자열 받기
json_input = sys.argv[1]

# 파싱
params = json.loads(json_input)

# 파라미터 사용
name = params.get("name", "이름없음")
age = params.get("age", 0)
city = params.get("city", "알 수 없음")
job = params.get("job", "직업 미상")

# 처리 결과 만들기
result = {
    "message": f"{name}님은 {age}세이며 {city}에 거주하는 {job}입니다.",
    "status": "success"
}

# JSON 문자열로 출력 (자바에서 읽을 수 있도록)
print(json.dumps(result, ensure_ascii=False))
