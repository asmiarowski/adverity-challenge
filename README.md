# Setup

## Requirements
- Docker
- Java 11

## Build application

To build the application run command in project directory:

```shell
./gradlew build
```
The build will run unit and integration tests, which can be skipped by adding `-x test`.

To generate API documentation in HTML, run

```shell
./gradlew build asciidoctor
```

The documentation will be available in `{projectDir}/build/asciidoc/html5/index.html`.

Postman collection is also available for convenience in `{projectDir}/postman` directory.

## Test application

To run the application locally, execute command in project directory:

```shell
docker-compose up
```

It will host the application under `http://localhost:10150`, the debug port is also open at `http://localhost:10151` allowing remote debugging through IDE.
MySQL is also exposed, you can find password and port in the `docker-compose.yml` file in project directory.

# How to load CSV

The CSV file with statistics is loaded automatically into docker volume. To load a different file, replace `{projectDir}/docker/campaign-daily-stats.csv` file.
To trigger import call `http://localhost:10150/import`

The call to API is done for simplicity, to be able to trigger it manually, but on production it wouldn't be necessary 
as we would likely have a cron scheduler to fetch new files from an external source.

This endpoint can be called only once. In case a complete refresh is needed, run `docker-compose down && docker-compose up`
to completely refresh database locally and repeat the call.

# API documentation

To build HTML API documentation see "Build application" section.

The documentation is generated from snippets in Controller unit tests which helps to ensure API documentation is always up to date.
The controller unit tests are not necessary since same thing (and more) is covered by integration tests, however, because 
integration tests are more costly. As the application grows it's beneficial to have them run on a separate Spring Profile, only 
executed by CI/CD pipelines or just before committing to the repository. It's also quicker to generate documentation based on unit tests.

# Performance

The CSV import saves 22k records in about 6 - 8s, further improvements could be done through changing the tool from 
Hibernate to something more performant for saving, however I've added some optimizations like: 
- ID generation in Java instead of MySQL
- Bulk insert

There's usually a tradeoff between solution complexity and performance and I think the outcome is acceptable for the needs,
as these are daily stats.

The read queries run pretty fast, examples below:

Daily stats without filter:
```sql
select campaignda0_.id as id1_0_, campaignda0_.campaign as campaign2_0_, campaignda0_.clicks as clicks3_0_, campaignda0_.ctr as ctr4_0_, campaignda0_.datasource as datasour5_0_, campaignda0_.day as day6_0_, campaignda0_.impressions as impressi7_0_ from campaign_daily_stats campaignda0_
    where campaignda0_.day>='2019-01-12' 
      and campaignda0_.day<='2019-12-14' 
limit 20;
```
The above query runs 50-70ms on my laptop, the query cost is 4750.80. I've tested BTREE index on day column, but the cost was multiplied by more than 3. Maybe on a bigger dataset it would be worth it.

Daily stats with filter:
```sql
select campaignda0_.id as id1_0_, campaignda0_.campaign as campaign2_0_, campaignda0_.clicks as clicks3_0_, campaignda0_.ctr as ctr4_0_, campaignda0_.datasource as datasour5_0_, campaignda0_.day as day6_0_, campaignda0_.impressions as impressi7_0_ from campaign_daily_stats campaignda0_
    where campaignda0_.day>='2019-01-12' 
      and campaignda0_.day<='2019-12-14' 
      and (campaignda0_.campaign in ('GDN_Retargeting' , 'DE|SN|Brand')) 
      and (campaignda0_.datasource in ('Google Ads')) 
limit 20
```
The above query runs 50-70ms on my laptop, the query cost is cheaper due to index usage 858.00.

Aggregated stats:
```sql
select 'campaign' as col_0_0_, campaignda0_.campaign as col_1_0_, sum(campaignda0_.impressions) as col_2_0_, sum(campaignda0_.clicks) as col_3_0_, 1000.0*sum(campaignda0_.clicks)/sum(campaignda0_.impressions) as col_4_0_ from campaign_daily_stats campaignda0_ 
    where campaignda0_.day>='2019-01-12' 
      and campaignda0_.day<='2019-12-14' 
group by campaignda0_.campaign 
limit 20
```
The above query runs 50-70ms on my laptop, the query cost is cheaper due to index usage 4750.80. Same cost as daily stats. 



