/* =========================================================
   1. MASTER TABLES
   ========================================================= */

-- Country Master
CREATE TABLE country (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) UNIQUE NOT NULL,   -- IN, US, UK
    name VARCHAR(100) NOT NULL,
    currency VARCHAR(10),
    timezone VARCHAR(50),
    active BOOLEAN DEFAULT TRUE
);

-- State / Region
CREATE TABLE state (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    country_id BIGINT NOT NULL,
    code VARCHAR(10),
    name VARCHAR(100),
    CONSTRAINT fk_state_country
        FOREIGN KEY (country_id) REFERENCES country(id)
);

/* =========================================================
   2. TENANT & AUTH
   ========================================================= */

-- Tenant
CREATE TABLE tenant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    type ENUM('COMPANY','VENDOR') NOT NULL,
    plan VARCHAR(50),
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tenant ↔ Country Mapping
CREATE TABLE tenant_country (
    tenant_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    PRIMARY KEY (tenant_id, country_id),
    CONSTRAINT fk_tc_tenant FOREIGN KEY (tenant_id) REFERENCES tenant(id),
    CONSTRAINT fk_tc_country FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Application Users
CREATE TABLE app_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM(
        'PLATFORM_ADMIN',
        'COMPANY_ADMIN',
        'RECRUITER',
        'VENDOR_ADMIN',
        'CANDIDATE'
    ) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_tenant FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);

/* =========================================================
   3. CANDIDATE MANAGEMENT
   ========================================================= */

CREATE TABLE candidate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NULL,
    country_id BIGINT NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mobile VARCHAR(20),
    total_experience DECIMAL(4,1),
    current_ctc DECIMAL(10,2),
    expected_ctc DECIMAL(10,2),
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_candidate_tenant FOREIGN KEY (tenant_id) REFERENCES tenant(id),
    CONSTRAINT fk_candidate_country FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE candidate_education (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    degree VARCHAR(100),
    institution VARCHAR(150),
    graduation_year INT,
    CONSTRAINT fk_edu_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

CREATE TABLE candidate_experience (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    company_name VARCHAR(150),
    designation VARCHAR(100),
    from_date DATE,
    to_date DATE,
    CONSTRAINT fk_exp_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

/* =========================================================
   4. SKILL MANAGEMENT
   ========================================================= */

CREATE TABLE `skill` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `tenant_id` BIGINT DEFAULT NULL COMMENT 'Tenant owning the skill; NULL = global',
    `country_id` BIGINT DEFAULT NULL COMMENT 'Optional country-specific skill',
    `name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50) DEFAULT NULL COMMENT 'Programming, DevOps, Soft Skill etc.',
    `active` BOOLEAN DEFAULT TRUE COMMENT 'Is this skill currently active?',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_skill_name` (`name`, `tenant_id`, `country_id`),
    KEY `idx_skill_tenant` (`tenant_id`),
    KEY `idx_skill_country` (`country_id`),
    KEY `idx_skill_category` (`category`),
    CONSTRAINT `fk_skill_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `tenant` (`id`),
    CONSTRAINT `fk_skill_country` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE candidate_skill (
    candidate_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency ENUM('BEGINNER','INTERMEDIATE','EXPERT'),
    PRIMARY KEY (candidate_id, skill_id),
    CONSTRAINT fk_cs_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    CONSTRAINT fk_cs_skill FOREIGN KEY (skill_id) REFERENCES skill(id)
);

/* =========================================================
   5. JOB MANAGEMENT
   ========================================================= */

CREATE TABLE job (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    experience_min DECIMAL(4,1),
    experience_max DECIMAL(4,1),
    job_type ENUM('FULL_TIME','PART_TIME','CONTRACT'),
    status ENUM('OPEN','CLOSED','ON_HOLD') DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_job_tenant FOREIGN KEY (tenant_id) REFERENCES tenant(id),
    CONSTRAINT fk_job_country FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE job_skill (
    job_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    mandatory BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (job_id, skill_id),
    CONSTRAINT fk_js_job FOREIGN KEY (job_id) REFERENCES job(id),
    CONSTRAINT fk_js_skill FOREIGN KEY (skill_id) REFERENCES skill(id)
);

/* =========================================================
   6. VENDOR / CONSULTANCY
   ========================================================= */

CREATE TABLE vendor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vendor_tenant FOREIGN KEY (tenant_id) REFERENCES tenant(id)
);

CREATE TABLE vendor_candidate (
    vendor_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    PRIMARY KEY (vendor_id, candidate_id),
    CONSTRAINT fk_vc_vendor FOREIGN KEY (vendor_id) REFERENCES vendor(id),
    CONSTRAINT fk_vc_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

/* =========================================================
   7. JOB APPLICATIONS
   ========================================================= */

CREATE TABLE job_application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    source ENUM('DIRECT','VENDOR') NOT NULL,
    vendor_id BIGINT NULL,
    status ENUM(
        'APPLIED',
        'SHORTLISTED',
        'INTERVIEW',
        'OFFERED',
        'HIRED',
        'REJECTED'
    ) DEFAULT 'APPLIED',
    ai_score DECIMAL(5,2),
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ja_job FOREIGN KEY (job_id) REFERENCES job(id),
    CONSTRAINT fk_ja_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    CONSTRAINT fk_ja_vendor FOREIGN KEY (vendor_id) REFERENCES vendor(id)
);

/* =========================================================
   8. AI & RESUME DATA
   ========================================================= */

CREATE TABLE resume (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    file_url VARCHAR(255),
    parsed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_resume_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

CREATE TABLE ai_match_result (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    job_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    match_score DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ai_job FOREIGN KEY (job_id) REFERENCES job(id),
    CONSTRAINT fk_ai_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

/* =========================================================
   9. INDEXES
   ========================================================= */
CREATE INDEX idx_candidate_email ON candidate(email);
CREATE INDEX idx_candidate_country ON candidate(country_id);
CREATE INDEX idx_job_tenant ON job(tenant_id);
CREATE INDEX idx_job_country ON job(country_id);
CREATE INDEX idx_job_application_job ON job_application(job_id);
CREATE INDEX idx_job_application_candidate ON job_application(candidate_id);



/* =========================================================
   10. Features
   ========================================================= */

CREATE TABLE feature (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150),
    description TEXT
);

/* =========================================================
   11. Features Mapped to country ❌ BGV disabled in some countries
✔ AI Resume Match allowed globally
   ========================================================= */
CREATE TABLE feature_country (
    feature_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (feature_id, country_id),
    FOREIGN KEY (feature_id) REFERENCES feature(id),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

/* =========================================================
   12. Features Mapped to State BGV allowed in California
Disabled in Texas
   ========================================================= */
CREATE TABLE feature_state (
    feature_id BIGINT NOT NULL,
    state_id BIGINT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (feature_id, state_id),
    FOREIGN KEY (feature_id) REFERENCES feature(id),
    FOREIGN KEY (state_id) REFERENCES state(id)
);

/* =========================================================
   13. Vendor X disabled BGV
    Company Y enabled AI Resume Boost
   ========================================================= */

CREATE TABLE tenant_feature (
    tenant_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (tenant_id, feature_id),
    FOREIGN KEY (tenant_id) REFERENCES tenant(id),
    FOREIGN KEY (feature_id) REFERENCES feature(id)
);


/* =========================================================
   14.Resume automation limit = 50
      Max AI score threshold
      BGV retry count
   ========================================================= */


CREATE TABLE feature_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feature_id BIGINT NOT NULL,
    country_id BIGINT NULL,
    state_id BIGINT NULL,
    config_key VARCHAR(50),
    config_value VARCHAR(100),
    FOREIGN KEY (feature_id) REFERENCES feature(id),
    FOREIGN KEY (country_id) REFERENCES country(id),
    FOREIGN KEY (state_id) REFERENCES state(id)
);

/* =========================================================
   15.Plan
   ========================================================= */

CREATE TABLE plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    user_type ENUM('CANDIDATE','COMPANY') NOT NULL,
    billing_type ENUM('SUBSCRIPTION','USAGE') NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Candidate Premium Plan
INSERT INTO plan (id, code, name, user_type, billing_type) VALUES
(1, 'CAND_PREMIUM', 'Candidate Premium', 'CANDIDATE', 'SUBSCRIPTION');

-- Company BGV Plan (Usage Based)
INSERT INTO plan (id, code, name, user_type, billing_type) VALUES
(2, 'COMPANY_BGV', 'Company Background Verification', 'COMPANY', 'USAGE');



/* =========================================================
   16.Plan ↔ Feature Mapping
   ========================================================= */
CREATE TABLE plan_feature (
    plan_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    usage_limit INT NULL, -- NULL = unlimited
    PRIMARY KEY (plan_id, feature_id),
    FOREIGN KEY (plan_id) REFERENCES plan(id),
    FOREIGN KEY (feature_id) REFERENCES feature(id)
);


/* =========================================================
   17.Country-wise Pricing (IMPORTANT)
   ========================================================= */
CREATE TABLE pricing (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    currency VARCHAR(10) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    billing_cycle ENUM('MONTHLY','YEARLY','PER_UNIT') NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES plan(id),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

/* =========================================================
   18.Candidate Subscription
   ========================================================= */
CREATE TABLE candidate_subscription (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status ENUM('ACTIVE','EXPIRED','CANCELLED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    FOREIGN KEY (plan_id) REFERENCES plan(id)
);


/* =========================================================
   19.Candidate Feature Usage (AI / Automation)
   ========================================================= */
CREATE TABLE candidate_feature_usage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    used_count INT DEFAULT 0,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    FOREIGN KEY (candidate_id) REFERENCES candidate(id),
    FOREIGN KEY (feature_id) REFERENCES feature(id)
);



/* =========================================================
   BGV Module
   ========================================================= */


/* =========================================================
   20.BGV Order (Billing Entity)
   ========================================================= */

   CREATE TABLE bgv_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2),
    currency VARCHAR(10),
    status ENUM(
        'CREATED',
        'PAID',
        'IN_PROGRESS',
        'COMPLETED',
        'FAILED'
    ) DEFAULT 'CREATED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenant(id),
    FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);


/* =========================================================
   21.BGV Order (Billing Entity)
   ========================================================= */

CREATE TABLE bgv_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bgv_order_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING','VERIFIED','FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bgv_order_id) REFERENCES bgv_order(id),
    FOREIGN KEY (feature_id) REFERENCES feature(id)
);


/* =========================================================
   PAYMENT & TRANSACTION MODULE
   ========================================================= */

/* =========================================================
   22.Payment
   ========================================================= */

   CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    payer_type ENUM('CANDIDATE','TENANT') NOT NULL,
    payer_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    gateway ENUM('RAZORPAY','STRIPE','PAYPAL') NOT NULL,
    status ENUM('INITIATED','SUCCESS','FAILED') DEFAULT 'INITIATED',
    reference_id VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

/* =========================================================
   23.Payment Item Mapping
   ========================================================= */
CREATE TABLE payment_item (
    payment_id BIGINT NOT NULL,
    entity_type ENUM('SUBSCRIPTION','BGV_ORDER') NOT NULL,
    entity_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (payment_id) REFERENCES payment(id)
);

/* =========================================================
   Indexes
   ========================================================= */
CREATE INDEX idx_pricing_plan_country ON pricing(plan_id, country_id);
CREATE INDEX idx_candidate_subscription_candidate ON candidate_subscription(candidate_id);
CREATE INDEX idx_feature_usage_candidate ON candidate_feature_usage(candidate_id);
CREATE INDEX idx_bgv_order_tenant ON bgv_order(tenant_id);
CREATE INDEX idx_payment_payer ON payment(payer_type, payer_id);