Aşağıdaki SQL sorguları ile tablolar oluşturmalı, persistence.xml içerisinde erişim bilgileri düzenlenmelidir. 

```
-- ----------------------------
-- Table structure for scheduled_jobs
-- ----------------------------
DROP TABLE IF EXISTS "public"."scheduled_jobs";
CREATE TABLE "public"."scheduled_jobs" (
  "id" int4 NOT NULL DEFAULT nextval('scheduled_jobs_id_seq'::regclass),
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "last_fired" timestamp(0),
  "next" timestamp(0),
  "period" int8,
  "log_enabled" bool
)
;

-- ----------------------------
-- Table structure for scheduled_job_logs
-- ----------------------------
DROP TABLE IF EXISTS "public"."scheduled_job_logs";
CREATE TABLE "public"."scheduled_job_logs" (
  "id" int4 NOT NULL DEFAULT nextval('scheduled_job_logs_id_seq'::regclass),
  "time" timestamp(0),
  "log" text COLLATE "pg_catalog"."default",
  "scheduled_job_id" int4
)
;

-- ----------------------------
-- Primary Key structure for table scheduled_job_logs
-- ----------------------------
ALTER TABLE "public"."scheduled_job_logs" ADD CONSTRAINT "scheduled_job_logs_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table scheduled_job_logs
-- ----------------------------
ALTER TABLE "public"."scheduled_job_logs" ADD CONSTRAINT "fk_scheduled_job_logs_scheduled_job_id" FOREIGN KEY ("scheduled_job_id") REFERENCES "public"."scheduled_jobs" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

```

scheduled_jobs tablosundaki period alanı saniye cinsinden doldurulmalı, name sınıfın paket ismi dahil tam adı olmalıdır.

Örneğin:

| name  | last_fired  | next  | period  | log_enabled  |
|---|---|---|---|---|
| scheduler.jobs.Tick  | 2020-07-20 14:29:16  |  2020-07-20 14:29:21 |  5 |  true |

Sadece Tomcat8 ile test edildi.
