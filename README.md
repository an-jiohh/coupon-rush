# Coupon Issuance – Limited Stock

선착순 한정 재고 기반의 쿠폰 발급 API 프로젝트

## 요구사항

### 쿠폰 발급 요청 API 구현  

한정된 재고로 선착순으로 재고처리, 누가 뭘 받았는지 알 수 있어야함  
선착순 처리 + 확률 기반 상품 배정  
UID는 path variable로 받아서 처리


- 재고 테이블  
  재고 등록 API는 따로 제공 하지 않고, 서버 구동 시 자동으로 Insert 되도록. 

- 상품
  - 상품 A : 재고 1개, 초기 확률 1%
  - 상품 B : 재고 30개, 초기 확률 10%
  - 상품 C : 재고 69개, 초기 확률 89%(나머지)
  - 중복 발급 불가(UID 단 1회)
  - 실제 추첨 시에는 가용 재고가 있는 상품만 대상으로 확률을 동적으로 재가중치하여 선택

### 동시성 테스트
- 테스트는 200명이 동시에 왔을 때, 처리할 수 있도록 구현
- 테스트 코드 작성, k6 말고 멀티 스레드로 테스트
- 중복 발급 불가

---

## 추가 요구사항

기본 요구사항을 바탕으로 추가적으로 있으면 좋을만한 요구사항

- [ ] 추후 쿠폰 발급 전략을 쉽게 바꿀 수 있도록 구현
  - 하루에 한번 쿠폰 초기화
- [ ] 추후 다른 쿠폰 이벤트 진행시 활용 가능

---

## 엔티티

### coupon

- id
- code  
  추후 쿠폰이름은 변경 될 수 있음
- name
- initial_stock
- remaining_stock
- prob_weight(ex A=1,B=10,C=89)
- created_at
- updated_at

### coupon_issues

- id
- uid (unique, index)
- coupon_code(FK->coupon.code)
- issued_at