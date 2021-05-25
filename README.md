# kotlin-study-03-04 - 도서 리뷰

## 도서 리뷰 앱

* 인터파크 Open API 를 통해 베스트셀러 정보를 가져와서 화면에 그릴 수 있음.
* 인터파크 Open API 를 통해 검색어에 해당하는 책 목록을 가져와서 화면에 그릴 수 있음.
* Local DB 를 이용하여 검색 기록을 저장하고 삭제할 수 있음.
* Local DB 를 이용하여 개인 리뷰를 저장할 수 있음.



## 이 챕터를 통해 배우는 것



* RecyclerView 사용하기
    *ListAdapter 와 dittUtil 클래스를 사용*
    > notifyDataSetChanged 는 지연이 길어지면 UX에 영향을 미치기 때문에, 가능한 적은 리소스와 함께 빠른 작업이 이루어져야 한다.
      목록 변경 시 호출하여 아이템 업데이트 하지만 비용이 많이든다.
      아를 이해 DiffUtil 클래스가 개발되었다.


* View Binding 사용하기
* Retrofit 사용하기 (API 호출)
* Glide 사용하기 (이미지 로딩)
* Android Room 사용하기

- 3가지 개념

    *Database (데이터베이스)*
    > 저장하는 데이터의 집합 단위

    *Entity(항목)*/
    > 데이터베이스 내의 테이블

    *DAO*/
    > 데이터베이스에 접근하는 함수(insert, update, delete)를 제공


* Open API 사용해보기

