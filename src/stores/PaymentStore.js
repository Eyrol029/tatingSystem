// src/stores/paymentStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const usePaymentStore = defineStore('payment', () => {

  // ---------- STATE ----------
  const soaList = ref([
    {
      id: 'SOA001',
      patientId: 'P001',
      patientName: 'Althea Mae Santos',
      date: '2026-05-14',
      otherDiagnosis: 'Normal Delivery',
      services: [
        { name: 'Maternity Package', amount: 15000 },
        { name: 'PHILHEALTH Process', amount: 600 }
      ],
      amountPaid: 0,
      receipts: [] // history of receipts tied to this SOA
    }
  ])

  const revenues = ref([])

  let receiptCounter = 1000 // for generating Invoice No.

  // ---------- HELPERS ----------
  function totalAmount(soa) {
    return soa.services.reduce((sum, s) => sum + s.amount, 0)
  }

  function remainingBalance(soa) {
    return totalAmount(soa) - soa.amountPaid
  }

  function soaStatus(soa) {
    const remaining = remainingBalance(soa)
    if (soa.amountPaid <= 0) return 'Unpaid'
    if (remaining > 0) return 'Partial'
    return 'Paid'
  }

  function generateInvoiceNo() {
    receiptCounter++
    return 'INV-' + receiptCounter
  }

  // ---------- ACTIONS ----------
  function addSOA(newSOA) {
    soaList.value.push({
      ...newSOA,
      id: 'SOA' + Math.floor(Math.random() * 9999),
      amountPaid: 0,
      receipts: []
    })
  }

  // Records a payment (partial or full) against an SOA.
  // Updates the SOA's amountPaid, logs a receipt, and adds a Revenue entry.
  function addReceipt(soaId, amountPaidNow) {
    const soa = soaList.value.find(s => s.id === soaId)
    if (!soa) return null

    const balanceBefore = remainingBalance(soa)
    const paymentAmount = Math.min(Number(amountPaidNow), balanceBefore)
    if (paymentAmount <= 0) return null

    const invoiceNo = generateInvoiceNo()

    soa.amountPaid += paymentAmount

    const receipt = {
      invoiceNo,
      soaId: soa.id,
      patientName: soa.patientName,
      amountPaid: paymentAmount,
      remainingBalance: remainingBalance(soa),
      date: new Date().toISOString().slice(0, 10)
    }
    soa.receipts.push(receipt)

    // Push corresponding Revenue entry
    revenues.value.push({
      id: Date.now(),
      dealer: soa.patientName,
      description: `Payment for ${soa.id}`,
      invoiceNo,
      amount: paymentAmount
    })

    return receipt
  }

  function addManualRevenue(entry) {
    revenues.value.push({
      id: Date.now(),
      ...entry
    })
  }

  function deleteRevenue(id) {
    revenues.value = revenues.value.filter(r => r.id !== id)
  }

  return {
    soaList,
    revenues,
    totalAmount,
    remainingBalance,
    soaStatus,
    addSOA,
    addReceipt,
    addManualRevenue,
    deleteRevenue
  }
})