Tables should be created with the following SQL queries, and access information should be edited in persistence.xml.

```
-- ----------------------------
-- Table structure for jobs
-- ----------------------------
DROP TABLE IF EXISTS "public"."jobs";
CREATE TABLE "public"."jobs" (
  "id" int4 NOT NULL DEFAULT nextval('jobs_id_seq'::regclass),
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "last_fired" timestamp(0),
  "next" timestamp(0),
  "period" int8,
  "log_enabled" bool,
  "days" varchar(255) COLLATE "pg_catalog"."default",
  "hour" time(6)
);

-- ----------------------------
-- Primary Key structure for table jobs
-- ----------------------------
ALTER TABLE "public"."jobs" ADD CONSTRAINT "jobs_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Table structure for job_logs
-- ----------------------------
DROP TABLE IF EXISTS "public"."job_logs";
CREATE TABLE "public"."job_logs" (
  "id" int4 NOT NULL DEFAULT nextval('job_logs_id_seq'::regclass),
  "time" timestamp(0),
  "log" text COLLATE "pg_catalog"."default",
  "job_id" int4
);

-- ----------------------------
-- Primary Key structure for table job_logs
-- ----------------------------
ALTER TABLE "public"."job_logs" ADD CONSTRAINT "job_logs_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Foreign Keys structure for table job_logs
-- ----------------------------
ALTER TABLE "public"."job_logs" ADD CONSTRAINT "fk_job_logs_job_id" FOREIGN KEY ("job_id") REFERENCES "public"."jobs" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

```

The period field in the scheduled_jobs table must be filled in seconds, and the name must be the full name of the class, including the package name.

Example:

| name  | last_fired  | next  | period  | log_enabled | days | hour |
| ----- | ----------- | ----- | ------- | ----------- | ---- | ---- |
| scheduler.jobs.Tick  | 2020-07-20 14:29:16  |  2020-07-20 14:29:21 |  5 |  true | null | null |

Example for daily job:
| name  | last_fired  | next  | period  | log_enabled | days | hour |
| ----- | ----------- | ----- | ------- | ----------- | ---- | ---- |
| scheduler.jobs.Tick  | 2020-07-20 14:29:16  |  null |  0 |  true | MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY | 10:00 |


Tested only with Tomcat8.
