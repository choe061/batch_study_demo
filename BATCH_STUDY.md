## Spring Batch
* Batch : 컴퓨터 데이터 처리 형태의 하나로써 처리해야 할 데이터를 일정 기간/일정량 정리하여 처리하는 것, 순차적으로 처리하는 것

#### Spring Boot Batch 2.0, Spring Batch 4.0
* spring boot batch 2.0은 spring batch 4.0 기반
* spring batch 4.0
    * 스프링 배치는 자바 버전과 써드 파티 디펜던시를 위한 Spring Framework의 baseline을 따라왔다. 그래서 Spring Batch 4는 최소 Spring Framework 5로 업그레이드 되었다. 이에 따라 Java 버전도 최소 8 이상을 요구한다. 내부적인 변화가 있는데, 가장 큰 것은 프레임워크가 함수형 프로그래밍과 람다가 지원된다.

#### ItemReaders, ItemProcessors, ItemWriters 빌더
* Spring Batch 4는 framework와 함께 ItemReader 구현과 ItemWriter 구현을 위한 빌더 Collection을 제공한다.
    * RepositoryItemReader - RepositoryItemReaderBuilder
    * RepositoryItemWriter - RepositoryItemWriterBuilder
    * MultiResourceItemReader - MultiResourceItemReaderBuilder
    * MultiResourceItemWriter - MultiResourceItemWriterBuilder
    * 등등...
* ItemReaders
* ItemProcessors
* ItemWriters

### 도메인 언어
* Spring Batch에서 사용되는 배치 Processing의 전반적인 개념은 쉽게 느껴지도록. **Jobs**, **Steps**와 **ItemReader**, **ItemWriter**라는 개발자가 제공받는 processing units이 있다.
* Spring patterns, 연산, 템플릿, 콜백, 관용덕분에 따라오는 opportunities가 있다.
    1. 관심사의 명확한 분리를 통해 adherence를 크게 개선할 수 있음
    2. 명확하게 묘사된 아키텍처 레이어와 인터페이스로 제공되는 서비스
    3. 빠르게 적용할 수 있는 간단한 default 구현체
    4. 크게 향상된 확장성
    
#### Job과 Step
* 배치는 하나의 Job을 가짐
* 하나의 Job은 여러 개의 Step으로 구성
    * Job : Step = 1 : N 의 관계

#### 구현 예제 내용
1. Read : 회원 정보 조회
2. Processing : 등급 재산정, 등급에 따라 쿠폰 발급
3. Write : 디비에 저장
