# Health Insurance Premium Estimator & Policy Tracker

A console-based Java application that calculates personalized health insurance premiums from age, coverage tier, and medical risk factors, and tracks customers and policies.

## Concepts demonstrated

| Concept | Where |
|---|---|
| Interface | `Premiumable` (premium + policy type contract) |
| Inheritance | `IndividualPolicy` and `FamilyFloaterPolicy` extend abstract `Policy` |
| Polymorphism | `Policy.calculatePremium()` calls each subclass's own `premiumBeforeTax()` |
| Enums | `CoverageTier`, `RiskFactor` |
| Collections | `HashMap` for ID lookups, `ArrayList` for policy lists, `EnumSet` for risk factors |
| Custom exceptions | `InvalidPolicyDataException`, `RecordNotFoundException` |
| SQL | `sql/schema.sql`: tables with primary keys, foreign keys, and CHECK constraints |

## Premium formula

```
Individual:      base x ageFactor x (1 + riskLoading)
Family floater:  base x ageFactor(eldest) x (1 + avgRiskLoading) x (1 + 0.30 x (members - 1))
Final premium  = result x 1.18   (18% GST)
```

| Tier | Sum insured | Base premium |
|---|---|---|
| BASIC | 3,00,000 | 5,000 |
| STANDARD | 5,00,000 | 9,000 |
| PREMIUM | 10,00,000 | 15,000 |

Age factor: under 18 = 0.8, 18-30 = 1.0, 31-45 = 1.3, 46-60 = 1.8, over 60 = 2.5.
Risk loading: smoker 25%, diabetes 20%, hypertension 15%, obesity 10%, heart disease 40% (capped at 100% total).

## Validation rules

- Name cannot be empty; age must be 0 to 100
- Individual policyholder must be 18 or older
- Family floater needs 2 to 6 members, no duplicates, and at least one adult
- Unknown customer IDs, policy IDs, tiers, or risk factors raise clear errors

## Run it

Requires JDK 17 or newer.

```
javac -d out $(find src -name "*.java")
java -cp out com.insurance.app.Main
```

On Windows PowerShell:

```
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out com.insurance.app.Main
```

## Database

`sql/schema.sql` creates `customer`, `risk_factor`, `customer_risk`, `policy`, and `policy_member`, plus sample data and reporting queries. It runs on MySQL and SQLite (use `PRAGMA foreign_keys = ON;` in SQLite).

## Project structure

```
health-insurance-estimator/
├── src/com/insurance/
│   ├── app/Main.java
│   ├── model/        Customer, Policy, IndividualPolicy, FamilyFloaterPolicy, Premiumable, enums
│   ├── service/PolicyManager.java
│   └── exception/    custom exceptions
├── sql/schema.sql
├── README.md
└── .gitignore
```

## Possible next steps

- Connect to the database with JDBC (SQLite or MySQL) instead of in-memory storage
- Add policy renewal dates and claims tracking
- Add JUnit tests for the premium calculations

## License

MIT
