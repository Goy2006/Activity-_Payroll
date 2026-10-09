/**
 * Project 1 - MCO1: Payroll / Contribution & Income Tax Computation (JavaScript version)
 * Same input and output as PayrollSystem.java
 *
 * Run: node payroll.js   (Node.js 17+)
 */
const readline = require("readline/promises");

// ---------- Contribution computations (employee share, monthly) ----------

// SSS: 5% employee share of Monthly Salary Credit (MSC), MSC 5,000 - 35,000
function computeSSS(salary) {
  let msc = Math.floor((salary + 250) / 500) * 500;
  msc = Math.max(5000, Math.min(35000, msc));
  return msc * 0.05;
}

// GSIS: 9% employee share of basic monthly salary (government employees)
function computeGSIS(salary) {
  return salary * 0.09;
}

// PhilHealth: 5% premium split equally -> 2.5% employee, salary floor 10,000 / ceiling 100,000
function computePhilHealth(salary) {
  const base = Math.max(10000, Math.min(100000, salary));
  return base * 0.025;
}

// Pag-IBIG: 2% employee share, max fund salary 10,000 (max 200)
function computePagibig(salary) {
  return Math.min(10000, salary) * 0.02;
}

// BIR withholding tax (TRAIN law, monthly table) on taxable income
function computeWithholdingTax(taxable) {
  if (taxable <= 20833) return 0;
  if (taxable <= 33333) return (taxable - 20833) * 0.15;
  if (taxable <= 66667) return 2500 + (taxable - 33333) * 0.2;
  if (taxable <= 166667) return 10833.33 + (taxable - 66667) * 0.25;
  if (taxable <= 666667) return 40833.33 + (taxable - 166667) * 0.3;
  return 200833.33 + (taxable - 666667) * 0.35;
}

const money = (v) =>
  v.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 });

const line = (label, value) =>
  label.padEnd(28) + " PHP " + money(value).padStart(15);

async function main() {
  const rl = readline.createInterface({ input: process.stdin, output: process.stdout });

  console.log("=== PAYROLL & INCOME TAX COMPUTATION ===");
  const name = (await rl.question("Employee name: ")).trim();
  const type = parseInt((await rl.question("Employee type (1 = Private, 2 = Government): ")).trim(), 10);
  const salary = parseFloat((await rl.question("Monthly basic salary: ")).trim());
  rl.close();

  const gov = type === 2;

  const sss = gov ? 0 : computeSSS(salary);
  const gsis = gov ? computeGSIS(salary) : 0;
  const philhealth = computePhilHealth(salary);
  const pagibig = computePagibig(salary);

  const totalContrib = sss + gsis + philhealth + pagibig;
  const taxable = salary - totalContrib;
  const tax = computeWithholdingTax(taxable);
  const netPay = taxable - tax;

  console.log();
  console.log("---------- PAYSLIP ----------");
  console.log("Employee : " + name);
  console.log("Type     : " + (gov ? "Government" : "Private"));
  console.log();
  console.log(line("Monthly Basic Salary", salary));
  console.log("Deductions:");
  if (gov) console.log(line("  GSIS (9%)", gsis));
  else console.log(line("  SSS (5% of MSC)", sss));
  console.log(line("  PhilHealth (2.5%)", philhealth));
  console.log(line("  Pag-IBIG (2%, max 200)", pagibig));
  console.log(line("Total Contributions", totalContrib));
  console.log(line("Taxable Income", taxable));
  console.log(line("Withholding Tax (BIR)", tax));
  console.log("-----------------------------------------------");
  console.log(line("NET PAY", netPay));
  console.log();
  console.log(line("Annual Gross Salary", salary * 12));
  console.log(line("Annual Tax Withheld", tax * 12));
}

main();