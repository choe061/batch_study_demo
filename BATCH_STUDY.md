## Spring Batch
* Batch : 컴퓨터 데이터 처리 형태의 하나로써 처리해야 할 데이터를 일정 기간/일정량 정리하여 처리하는 것, 순차적으로 처리하는 것

#### Spring Boot Batch 2.0, Spring Batch 4.0
* spring boot batch 2.0은 spring batch 4.0 기반
* spring batch 4.0
    * 스프링 배치는 자바 버전과 써드 파티 디펜던시를 위한 Spring Framework의 baseline을 따라왔다. 그래서 Spring Batch 4는 최소 Spring Framework 5로 업그레이드 되었다. 이에 따라 Java 버전도 최소 8 이상을 요구한다. 내부적인 변화가 있는데, 가장 큰 것은 프레임워크가 함수형 프로그래밍과 람다가 지원된다.

#### Spring Batch Architecture
* 스프링 배치는 확장성있게 디자인되어 있음
* Spring Batch의 Layer architecture, 아래 세 가지는 high-level component
    * __Application__
        * 모든 batch jobs
        * 개발자가 코딩한 것들
    * __Batch Core__
        * Batch job을 컨트롤하고 시작시키기 위해 필수적인 core 클래스들
        * JobLauncher, Job, Step의 구현체 등등
    * __Batch Infrastructure__
        * Application과 Batch Core는 Common Infrastructure 를 기반으로 짓는다
        * Common readers, writers
            * ItemReader, ItemWriter
        * services(RetryTemplate 같은 것)
* 배치의 processing options
    1. 오프라인 모드로 일괄 처리
        * single commit 이기 때문에 concurrency 이슈 없다.
    2. 동시 batch 와 온라인 processing
        * batch 작업을 수행하는 동안 db를 lock 할 수 없으니, 그 부분에 대해 고려해야함
        * 시간 값을 컬럼으로 추가
    3. 병렬 처리 (Parallel Processing)
        * multiple batch or job
        * batch processing time 을 최소화하기 위해 사용
    4. 파티셔닝 (Partitioning)
        * 여러 버전을 동시에 실행하기 위해 파티셔닝을 사용
        * 파티셔닝 또한 긴 배치 jobs의 처리 시간을 줄이기 위함
        * input file을 분리할 수 있거나 디비 테이블이 분할되어 다른 데이터 셋을 구성할 수 있으면 파티셔닝으로 처리할 수 있다
            * 분할된 프로세스는 할당받은 데이터 셋만 처리하고, 다른 데이터 셋에 간섭하지 않도록 개발해야 한다

#### ItemReaders, ItemProcessors, ItemWriters 빌더
* Spring Batch 4는 framework와 함께 ItemReader 구현과 ItemWriter 구현을 위한 빌더 Collection을 제공한다.
    * PagingItemReader
        * JdbcPagingItemReader
            * iBatisPagingItemReader : 최근 버전에서 deprecated, JdbcPaging~~ 사용
        * JpaPagingItemReader
        * HibernatePagingItemReader
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
    
#### Batch.core 코드 중 일부
* JobBuilder, StepBuilder
    * 둘다 JobBuilderHelper, StepBuilderHelper를 상속/구현하고 builder 패턴이 추상클래스로 구현되어 있음
    * Helper 클래스에서는 제네릭으로 상속받는 자식 클래스의 타입을 받아서 타입 변환을 해준다. 빌더 패턴은 자기 자신을 그대로 리턴해서 체이닝으로 연결해줘야하기 때문임
    * 이런식으로 빌더패턴도 공통 기능이 있다면 추상화할 수 있네

#### 구현 예제 내용
1. Read : 회원 정보 조회
2. Processing : 등급 재산정, 등급에 따라 쿠폰 발급
3. Write : 디비에 저장
