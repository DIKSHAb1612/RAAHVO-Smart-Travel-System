# RAAHVO - APPLICATION FLOWCHART & STATE MACHINE

## 1. System Navigation Flowchart

```mermaid
flowchart TD
    Start([Launch RAAHVO Console]) --> Init[Load db.properties & Init Connection]
    Init --> Banner[Render ANSI Banner & Tagline]
    Banner --> Menu{Main Navigation Menu}

    Menu -->|Choice 1| Planner[Smart Budget Trip Planner]
    Menu -->|Choice 2| Explore[Explore Destinations & Costs]
    Menu -->|Choice 3| Transport[Search & Book Transport]
    Menu -->|Choice 4| Hotel[Search & Book Hotels]
    Menu -->|Choice 5| Packages[Browse & Compare Packages]
    Menu -->|Choice 6| Dashboard[User Login / Dashboard]
    Menu -->|Choice 7| Register[Register New Account]
    Menu -->|Choice 8| Admin[Admin Control Panel]
    Menu -->|Choice 9| Stress[Multithreading Concurrency Demo]
    Menu -->|Choice 0| Exit([Exit System])

    Planner --> Inputs[Input: Source, Dest, Pax, Days, Budget, Style]
    Inputs --> Engine[TripPlanner: Query Real Transports, Hotels & Spots]
    Engine --> Calc[BudgetCalculator: Itemize Base + GST + Food]
    Calc --> Compare{Evaluate Options vs Budget}
    Compare --> RenderOptions[Display 3 Dynamic Tiers: Budget, Standard, Premium]
    RenderOptions --> PromptBook{Book an Option Now?}
    PromptBook -->|Yes| UniversalCheckout[Universal Booking & Payment Workflow]
    PromptBook -->|No| Menu

    UniversalCheckout --> AuthCheck{Is User Logged In?}
    AuthCheck -->|No| QuickAuth[Login or Quick Registration]
    AuthCheck -->|Yes| PaxEntry[Collect Passenger Details & Govt IDs]
    QuickAuth --> PaxEntry
    PaxEntry --> CouponStep{Apply Discount Coupon?}
    CouponStep -->|Yes| ValidateCoupon[Validate Code & Subtract Discount]
    CouponStep -->|No| PayGateway[Select Simulated Payment Gateway: UPI/Card/NetBank]
    ValidateCoupon --> PayGateway
    PayGateway --> TxnExec[Execute Atomic JDBC Transaction / Seat Locks]
    TxnExec --> SuccessConfirm[Generate Unique PNR & Display Confirmation]
    SuccessConfirm --> FileExport[Export Formatted Invoice to invoices/INVOICE_PNR.txt]
    FileExport --> Menu

    Dashboard --> UserActions{User Actions}
    UserActions --> ViewHistory[Display Booking History & Sorted Tree]
    UserActions --> CancelBooking[Cancel Reservation & Process Refund]
    UserActions --> EditProfile[Update Phone / Change Password]
    UserActions --> Logout[Logout & Clear Session]
    UserActions --> Menu
```

## 2. Smart Trip Planner Decision Logic
```mermaid
flowchart TD
    UserQuery[User inputs: City, Manali, 2 Travellers, 3 Days, Budget: ₹15,000] --> QueryDB[Query DB for routes, hotels, spots]
    QueryDB --> BuildBudget[Build Tier 1: Budget<br/>Volvo Bus + Solang Retreat + 2 Spots]
    QueryDB --> BuildStandard[Build Tier 2: Standard<br/>Vande Bharat + Himalayan Resort + 3 Spots]
    QueryDB --> BuildPremium[Build Tier 3: Premium<br/>Flight + Luxury Suite + All Spots]

    BuildBudget --> Check1{₹13,590 <= ₹15,000?}
    Check1 -->|True| MarkBudgetGreen[Mark Option 1: WITHIN BUDGET (+₹1,410 cushion)]
    BuildStandard --> Check2{₹24,145 <= ₹15,000?}
    Check2 -->|False| MarkStdRed[Mark Option 2: EXCEEDS BUDGET by ₹9,145]
    BuildPremium --> Check3{₹49,291 <= ₹15,000?}
    Check3 -->|False| MarkPremRed[Mark Option 3: EXCEEDS BUDGET by ₹34,291]

    MarkBudgetGreen --> Present[Present Side-by-Side Breakdown to User]
    MarkStdRed --> Present
    MarkPremRed --> Present
```
