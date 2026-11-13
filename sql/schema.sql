-- Health Insurance Premium Estimator: database schema
-- Works on MySQL and SQLite (enable foreign keys in SQLite: PRAGMA foreign_keys = ON;)

CREATE TABLE customer (
    customer_id  VARCHAR(10)  PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    age          INT          NOT NULL CHECK (age BETWEEN 0 AND 100)
);

CREATE TABLE risk_factor (
    risk_id      INT          PRIMARY KEY,
    name         VARCHAR(30)  NOT NULL UNIQUE,
    loading_pct  DECIMAL(5,2) NOT NULL CHECK (loading_pct >= 0)
);

-- Many-to-many: a customer can have several risk factors
CREATE TABLE customer_risk (
    customer_id  VARCHAR(10) NOT NULL,
    risk_id      INT         NOT NULL,
    PRIMARY KEY (customer_id, risk_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (risk_id)     REFERENCES risk_factor(risk_id)
);

CREATE TABLE policy (
    policy_id    VARCHAR(10)   PRIMARY KEY,
    policy_type  VARCHAR(20)   NOT NULL CHECK (policy_type IN ('INDIVIDUAL', 'FAMILY_FLOATER')),
    tier         VARCHAR(10)   NOT NULL CHECK (tier IN ('BASIC', 'STANDARD', 'PREMIUM')),
    sum_insured  DECIMAL(12,2) NOT NULL,
    premium      DECIMAL(12,2) NOT NULL
);

-- Which customers are covered by which policy (1 row for Individual, 2 to 6 for Family Floater)
CREATE TABLE policy_member (
    policy_id    VARCHAR(10) NOT NULL,
    customer_id  VARCHAR(10) NOT NULL,
    PRIMARY KEY (policy_id, customer_id),
    FOREIGN KEY (policy_id)   REFERENCES policy(policy_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Reference data
INSERT INTO risk_factor VALUES (1, 'SMOKER', 25.00);
INSERT INTO risk_factor VALUES (2, 'DIABETES', 20.00);
INSERT INTO risk_factor VALUES (3, 'HYPERTENSION', 15.00);
INSERT INTO risk_factor VALUES (4, 'OBESITY', 10.00);
INSERT INTO risk_factor VALUES (5, 'HEART_DISEASE', 40.00);

-- Sample data
INSERT INTO customer VALUES ('C001', 'Asha Rao', 34);
INSERT INTO customer VALUES ('C002', 'Ravi Rao', 38);
INSERT INTO customer VALUES ('C003', 'Mira Rao', 8);
INSERT INTO customer_risk VALUES ('C002', 1);
INSERT INTO customer_risk VALUES ('C002', 3);

INSERT INTO policy VALUES ('P001', 'INDIVIDUAL', 'STANDARD', 500000, 13806.00);
INSERT INTO policy_member VALUES ('P001', 'C001');

INSERT INTO policy VALUES ('P002', 'FAMILY_FLOATER', 'PREMIUM', 1000000, 41724.80);
INSERT INTO policy_member VALUES ('P002', 'C001');
INSERT INTO policy_member VALUES ('P002', 'C002');
INSERT INTO policy_member VALUES ('P002', 'C003');

-- Useful queries
-- 1. All policies with their holders
SELECT p.policy_id, p.policy_type, p.tier, c.name
FROM policy p
JOIN policy_member pm ON pm.policy_id = p.policy_id
JOIN customer c       ON c.customer_id = pm.customer_id
ORDER BY p.policy_id;

-- 2. Customers with risk factors
SELECT c.name, r.name AS risk
FROM customer c
JOIN customer_risk cr ON cr.customer_id = c.customer_id
JOIN risk_factor r    ON r.risk_id = cr.risk_id;

-- 3. Total premium per policy type
SELECT policy_type, COUNT(*) AS policies, SUM(premium) AS total_premium
FROM policy
GROUP BY policy_type;
