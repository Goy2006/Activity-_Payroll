import java.util.Scanner;

/**
 * Project 1 - MCO1: Payroll / Contribution & Income Tax Computation (Java version)
 * Same input and output as payroll.js
 *
 * Compile: javac PayrollSystem.java
 * Run:     java PayrollSystem
 */
public class PayrollSystem {

    // ---------- Contribution computations (employee share, monthly) ----------

    // SSS: 5% employee share of Monthly Salary Credit (MSC), MSC 5,000 - 35,000
    static double computeSSS(double salary) {
        double msc = Math.floor((salary + 250) / 500) * 500;
        msc = Math.max(5000, Math.min(35000, msc));
        return msc * 0.05;
    }

    // GSIS: 9% employee share of basic monthly salary (government employees)
    static double computeGSIS(double salary) {
        return salary * 0.09;
    }

    // PhilHealth: 5% premium split equally -> 2.5% employee, salary floor 10,000 / ceiling 100,000
    static double computePhilHealth(double salary) {
        double base = Math.max(10000, Math.min(100000, salary));
        return base * 0.025;
    }

    // Pag-IBIG: 2% employee share, max fund salary 10,000 (max 200)
    static double computePagibig(double salary) {
        double base = Math.min(10000, salary);
        return base * 0.02;
    }

    // BIR withholding tax (TRAIN law, monthly table) on taxable income
    static double computeWithholdingTax(double taxable) {
        if (taxable <= 20833) return 0;
        if (taxable <= 33333) return (taxable - 20833) * 0.15;
        if (taxable <= 66667) return 2500 + (taxable - 33333) * 0.20;
        if (taxable <= 166667) return 10833.33 + (taxable - 66667) * 0.25;
        if (taxable <= 666667) return 40833.33 + (taxable - 166667) * 0.30;
        return 200833.33 + (taxable - 666667) * 0.35;
    }

    static String money(double v) {
        return String.format("%,.2f", v);
    }

    static String line(String label, double value) {
        return String.format("%-28s PHP %15s", label, money(value));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== PAYROLL & INCOME TAX COMPUTATION ===");
        System.out.print("Employee name: ");
        String name = sc.nextLine().trim();
        System.out.print("Employee type (1 = Private, 2 = Government): ");
        int type = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Monthly basic salary: ");
        double salary = Double.parseDouble(sc.nextLine().trim());
        sc.close();

        boolean gov = (type == 2);

        double sss = gov ? 0 : computeSSS(salary);
        double gsis = gov ? computeGSIS(salary) : 0;
        double philhealth = computePhilHealth(salary);
        double pagibig = computePagibig(salary);

        double totalContrib = sss + gsis + philhealth + pagibig;
        double taxable = salary - totalContrib;
        double tax = computeWithholdingTax(taxable);
        double netPay = taxable - tax;

        System.out.println();
        System.out.println("---------- PAYSLIP ----------");
        System.out.println("Employee : " + name);
        System.out.println("Type     : " + (gov ? "Government" : "Private"));
        System.out.println();
        System.out.println(line("Monthly Basic Salary", salary));
        System.out.println("Deductions:");
        if (gov) System.out.println(line("  GSIS (9%)", gsis));
        else System.out.println(line("  SSS (5% of MSC)", sss));
        System.out.println(line("  PhilHealth (2.5%)", philhealth));
        System.out.println(line("  Pag-IBIG (2%, max 200)", pagibig));
        System.out.println(line("Total Contributions", totalContrib));
        System.out.println(line("Taxable Income", taxable));
        System.out.println(line("Withholding Tax (BIR)", tax));
        System.out.println("-----------------------------------------------");
        System.out.println(line("NET PAY", netPay));
        System.out.println();
        System.out.println(line("Annual Gross Salary", salary * 12));
        System.out.println(line("Annual Tax Withheld", tax * 12));
    }
}