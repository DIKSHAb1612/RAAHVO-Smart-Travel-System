# RAAHVO - PROBLEM STATEMENT

## 1. Context & Background
Modern leisure and business travelers face significant cognitive fatigue and financial uncertainty when planning trips. Current travel booking ecosystems are fragmented: users must individually browse rail ticketing portals (such as IRCTC), state/private bus booking apps, flight aggregator engines, and distinct hotel reservation platforms.

Crucially, **budget-constrained travelers** (such as university students, backpackers, young professionals, and middle-income families) struggle to answer the fundamental question:
> *"Given a fixed budget of ₹15,000 and 2 travellers for 3 days, where can we travel comfortably, what transport should we take, where can we stay, and will we face hidden expenses?"*

Existing platforms either push expensive sponsored packages or require manual calculation across spreadsheets, leading to booking friction, sudden overdrafts, and suboptimal itinerary decisions.

## 2. Core Problem Statement
There is a distinct absence of a **unified, transparent, budget-governed travel planning engine** that combines multi-modal transportation (trains, buses, flights), destination lodging, and sightseeing estimates while dynamically evaluating financial feasibility in real-time.

Traditional software solutions suffer from:
1. **Lack of Unified Budget Synthesis**: Inability to generate multi-tiered options (Budget, Standard, Premium) from real schedule tariffs and hotel night rates.
2. **Hidden Expenditure Surprises**: Failure to include mandatory local taxes (GST), daily sustenance, and attraction entry fees.
3. **Overbooking Race Conditions**: Concurrency vulnerabilities when multiple users attempt to reserve limited seats or rooms simultaneously.
4. **Poor Architecture in Academic Projects**: Monolithic codebases with tightly coupled UI, absence of clean OOP principles, and simulated hardcoded data rather than genuine relational structures.

## 3. The RAAHVO Solution
**RAAHVO** (*"Every Route Has a Story"*) solves this challenge by serving as an intelligent, modular, console-based Java application backed by Microsoft SQL Server. It empowers travelers to dynamically plan trips, search multi-modal transport, reserve hotel rooms, apply promo vouchers, and receive atomic booking confirmations with exported legal invoices, all protected by synchronized thread safety.
