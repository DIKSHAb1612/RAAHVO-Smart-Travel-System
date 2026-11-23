package com.raahvo.member1;

/**
 * ============================================================================
 * RAAHVO - Smart Travel Booking & Trip Planning System
 * Module: Member 1 - User & Destination Management
 * ============================================================================
 * Class: Admin
 * Course Concept: Inheritance & Subclassing (OOP)
 * 
 * Purpose:
 * Represents system administrators with elevated operational permissions.
 * Extends base class User, reusing common identity attributes while adding
 * administrative department and privilege rankings.
 */
public class Admin extends User {
    // Specific operational division (e.g. Operations, IT, Customer Support)
    private String department;
    
    // Administrative tier (1 = Read Only, 5 = Super Admin Full Access)
    private int privilegeLevel;

    /**
     * Parameterized Constructor
     * Demonstrates 'super(...)' to invoke the parent User constructor,
     * enforcing role = "ADMIN" automatically.
     */
    public Admin(int userId, String name, String email, String phone, String passwordHash, String department, int privilegeLevel) {
        super(userId, name, email, phone, passwordHash, "ADMIN");
        this.department = department;
        this.privilegeLevel = privilegeLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    public void setPrivilegeLevel(int privilegeLevel) {
        this.privilegeLevel = privilegeLevel;
    }

    /**
     * Method Overriding (Polymorphism): Specializes toString for Admin output
     */
    @Override
    public String toString() {
        return String.format("Admin[ID=%d, Name='%s', Dept='%s', PrivilegeLevel=%d]",
                getUserId(), getName(), department, privilegeLevel);
    }
}
