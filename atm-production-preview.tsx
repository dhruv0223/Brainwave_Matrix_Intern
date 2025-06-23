"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { CreditCard, DollarSign, History, PiggyBank, Check, AlertCircle } from "lucide-react"

type Screen = "welcome" | "enter-account" | "enter-pin" | "main-menu" | "withdraw-amount" | "deposit-amount" | "history"

type Account = {
  accountNumber: string
  pin: string
  name: string
  balance: number
}

type Transaction = {
  type: string
  amount: number
  balance: number
  date: string
  description: string
}

type NotificationType = {
  show: boolean
  success: boolean
  title: string
  message: string
}

export default function ATMProductionPreview() {
  const [currentScreen, setCurrentScreen] = useState<Screen>("welcome")
  const [inputBuffer, setInputBuffer] = useState("")
  const [tempAccountNumber, setTempAccountNumber] = useState("")
  const [cardInserted, setCardInserted] = useState(false)
  const [currentAccount, setCurrentAccount] = useState<Account | null>(null)
  const [notification, setNotification] = useState<NotificationType>({
    show: false,
    success: false,
    title: "",
    message: "",
  })

  const accounts: Account[] = [
    { accountNumber: "1234567890", pin: "1234", name: "John Doe", balance: 1500.0 },
    { accountNumber: "0987654321", pin: "5678", name: "Jane Smith", balance: 2500.0 },
    { accountNumber: "1111222233", pin: "9999", name: "Bob Johnson", balance: 750.5 },
    { accountNumber: "4444555566", pin: "0000", name: "Alice Williams", balance: 3200.75 },
    { accountNumber: "7777888899", pin: "1111", name: "Michael Brown", balance: 890.25 },
    { accountNumber: "2222333344", pin: "2222", name: "Sarah Davis", balance: 4500.0 },
    { accountNumber: "5555666677", pin: "3333", name: "David Wilson", balance: 1200.8 },
    { accountNumber: "8888999900", pin: "4444", name: "Emma Garcia", balance: 2100.45 },
  ]

  const [transactions] = useState<Transaction[]>([
    { type: "DEPOSIT", amount: 1500.0, balance: 1500.0, date: "2024-01-15 09:00:00", description: "Initial deposit" },
    { type: "WITHDRAWAL", amount: 100.0, balance: 1400.0, date: "2024-01-15 14:30:00", description: "Cash withdrawal" },
    { type: "DEPOSIT", amount: 200.0, balance: 1600.0, date: "2024-01-16 10:15:00", description: "Cash deposit" },
    {
      type: "TRANSFER_OUT",
      amount: 100.0,
      balance: 1500.0,
      date: "2024-01-16 16:45:00",
      description: "Transfer to 0987654321",
    },
  ])

  const showNotification = (success: boolean, title: string, message: string) => {
    setNotification({ show: true, success, title, message })
    setTimeout(() => {
      setNotification((prev) => ({ ...prev, show: false }))
    }, 3000)
  }

  const handleKeypadInput = (input: string) => {
    if (input === "CLR") {
      setInputBuffer("")
    } else if (input === "ENT") {
      processInput()
    } else if (inputBuffer.length < 20) {
      setInputBuffer((prev) => prev + input)
    }
  }

  const processInput = () => {
    const input = inputBuffer
    setInputBuffer("")

    switch (currentScreen) {
      case "enter-account":
        if (input.length >= 8) {
          setTempAccountNumber(input)
          setCurrentScreen("enter-pin")
        } else {
          showNotification(false, "Invalid Account Number", "Account number must be at least 8 digits")
        }
        break

      case "enter-pin":
        if (input.length === 4) {
          const account = accounts.find((acc) => acc.accountNumber === tempAccountNumber && acc.pin === input)
          if (account) {
            setCurrentAccount(account)
            setCurrentScreen("main-menu")
            showNotification(true, "Login Successful", `Welcome, ${account.name}!`)
          } else {
            showNotification(false, "Authentication Failed", "Invalid account number or PIN")
            resetSession()
          }
        } else {
          showNotification(false, "Invalid PIN", "PIN must be exactly 4 digits")
        }
        break

      case "withdraw-amount":
        const withdrawAmount = Number.parseFloat(input)
        if (withdrawAmount > 0 && withdrawAmount % 20 === 0) {
          if (currentAccount && currentAccount.balance >= withdrawAmount) {
            setCurrentAccount((prev) => (prev ? { ...prev, balance: prev.balance - withdrawAmount } : null))
            showNotification(
              true,
              "Withdrawal Successful",
              `Amount: $${withdrawAmount.toFixed(2)}\nRemaining Balance: $${(currentAccount.balance - withdrawAmount).toFixed(2)}`,
            )
            setCurrentScreen("main-menu")
          } else {
            showNotification(false, "Withdrawal Failed", "Insufficient funds")
          }
        } else {
          showNotification(false, "Invalid Amount", "Amount must be positive and in multiples of $20")
        }
        break

      case "deposit-amount":
        const depositAmount = Number.parseFloat(input)
        if (depositAmount > 0) {
          if (currentAccount) {
            setCurrentAccount((prev) => (prev ? { ...prev, balance: prev.balance + depositAmount } : null))
            showNotification(
              true,
              "Deposit Successful",
              `Amount: $${depositAmount.toFixed(2)}\nNew Balance: $${(currentAccount.balance + depositAmount).toFixed(2)}`,
            )
            setCurrentScreen("main-menu")
          }
        } else {
          showNotification(false, "Invalid Amount", "Amount must be positive")
        }
        break
    }
  }

  const handleMenuAction = (action: string) => {
    switch (action) {
      case "balance":
        if (currentAccount) {
          showNotification(
            true,
            "Balance Inquiry",
            `Account: ${currentAccount.accountNumber}\nHolder: ${currentAccount.name}\nBalance: $${currentAccount.balance.toFixed(2)}`,
          )
        }
        break
      case "withdraw":
        setCurrentScreen("withdraw-amount")
        break
      case "deposit":
        setCurrentScreen("deposit-amount")
        break
      case "history":
        setCurrentScreen("history")
        break
    }
  }

  const resetSession = () => {
    setCardInserted(false)
    setCurrentAccount(null)
    setCurrentScreen("welcome")
    setInputBuffer("")
    setTempAccountNumber("")
  }

  const insertCard = () => {
    setCardInserted(true)
    setCurrentScreen("enter-account")
  }

  const renderMainDisplay = () => {
    switch (currentScreen) {
      case "welcome":
        return (
          <div className="text-center text-white space-y-6">
            <div className="text-6xl mb-4">💳</div>
            <div className="text-2xl font-bold">Welcome to Secure Bank</div>
            <div className="text-lg">Please insert your card to begin</div>
            <div className="w-48 h-1 bg-blue-400 mx-auto rounded"></div>
            <div className="text-sm text-blue-200">Insert card above ↑</div>
          </div>
        )

      case "enter-account":
        return (
          <div className="text-center text-white space-y-6">
            <div className="text-xl font-bold">Enter Account Number</div>
            <div className="text-lg">Account Number:</div>
            <div className="bg-gray-800 p-4 rounded font-mono text-xl min-h-[60px] flex items-center justify-center">
              {inputBuffer || "_"}
            </div>
            <div className="text-sm text-blue-200">Press ENT when complete</div>
          </div>
        )

      case "enter-pin":
        return (
          <div className="text-center text-white space-y-6">
            <div className="text-xl font-bold">Enter PIN</div>
            <div className="text-lg">PIN:</div>
            <div className="bg-gray-800 p-4 rounded font-mono text-xl min-h-[60px] flex items-center justify-center">
              {"*".repeat(inputBuffer.length)}
            </div>
            <div className="text-sm text-blue-200">Enter 4-digit PIN and press ENT</div>
          </div>
        )

      case "main-menu":
        return (
          <div className="text-center text-white space-y-4">
            <div className="text-xl font-bold">Welcome, {currentAccount?.name}</div>
            <div className="text-sm">Account: {currentAccount?.accountNumber}</div>
            <div className="text-sm">Balance: ${currentAccount?.balance.toFixed(2)}</div>
            <div className="text-lg mt-8">Select a transaction from the menu</div>
            <div className="text-sm text-blue-200">Use the buttons on the left</div>
          </div>
        )

      case "withdraw-amount":
        return (
          <div className="text-center text-white space-y-6">
            <div className="text-xl font-bold">Cash Withdrawal</div>
            <div className="text-sm">Current Balance: ${currentAccount?.balance.toFixed(2)}</div>
            <div className="text-lg">Enter Amount:</div>
            <div className="bg-gray-800 p-4 rounded font-mono text-xl min-h-[60px] flex items-center justify-center">
              ${inputBuffer || "0.00"}
            </div>
            <div className="text-sm text-blue-200">Amount must be in multiples of $20</div>
          </div>
        )

      case "deposit-amount":
        return (
          <div className="text-center text-white space-y-6">
            <div className="text-xl font-bold">Cash Deposit</div>
            <div className="text-sm">Current Balance: ${currentAccount?.balance.toFixed(2)}</div>
            <div className="text-lg">Enter Amount:</div>
            <div className="bg-gray-800 p-4 rounded font-mono text-xl min-h-[60px] flex items-center justify-center">
              ${inputBuffer || "0.00"}
            </div>
            <div className="text-sm text-blue-200">Press ENT when complete</div>
          </div>
        )

      case "history":
        return (
          <div className="text-white space-y-4 text-xs">
            <div className="text-lg font-bold text-center">Transaction History</div>
            <div className="text-center text-sm">
              {currentAccount?.name} - {currentAccount?.accountNumber}
            </div>
            <div className="bg-gray-800 p-4 rounded">
              <div className="font-mono space-y-1">
                <div className="font-bold border-b pb-1">TYPE | AMOUNT | BALANCE | DATE & TIME | DESCRIPTION</div>
                {transactions.map((tx, idx) => (
                  <div key={idx} className="text-xs">
                    {tx.type} | ${tx.amount.toFixed(2)} | ${tx.balance.toFixed(2)} | {tx.date} | {tx.description}
                  </div>
                ))}
              </div>
            </div>
            <div className="text-center">
              <Button onClick={() => setCurrentScreen("main-menu")} className="bg-blue-600 hover:bg-blue-700">
                Back to Menu
              </Button>
            </div>
          </div>
        )

      default:
        return null
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-600 to-blue-800 p-4">
      {/* Window Title Bar */}
      <div className="max-w-7xl mx-auto mb-4">
        <div className="bg-gray-800 text-white px-4 py-2 rounded-t-lg flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <div className="flex space-x-1">
              <div className="w-3 h-3 bg-red-500 rounded-full"></div>
              <div className="w-3 h-3 bg-yellow-500 rounded-full"></div>
              <div className="w-3 h-3 bg-green-500 rounded-full"></div>
            </div>
            <span className="ml-4 font-semibold">Secure Bank ATM - Desktop Application</span>
          </div>
          <span className="text-sm text-gray-300">v2.1.0</span>
        </div>
      </div>

      {/* Main ATM Interface */}
      <div className="max-w-7xl mx-auto bg-blue-600 rounded-b-lg shadow-2xl">
        <div className="flex">
          {/* Left Panel - Menu and Card Reader */}
          <div className="w-64 bg-blue-600 p-6 space-y-6">
            <div className="text-white">
              <h1 className="text-2xl font-bold mb-8">SECURE BANK</h1>

              {/* Menu Buttons */}
              <div className="space-y-3">
                <Button
                  onClick={() => handleMenuAction("balance")}
                  disabled={!currentAccount}
                  className="w-full justify-start bg-blue-700 hover:bg-blue-800 text-white disabled:bg-blue-800 disabled:opacity-50"
                >
                  <DollarSign className="w-4 h-4 mr-2" />
                  Check Balance
                </Button>
                <Button
                  onClick={() => handleMenuAction("withdraw")}
                  disabled={!currentAccount}
                  className="w-full justify-start bg-blue-700 hover:bg-blue-800 text-white disabled:bg-blue-800 disabled:opacity-50"
                >
                  <CreditCard className="w-4 h-4 mr-2" />
                  Withdraw Cash
                </Button>
                <Button
                  onClick={() => handleMenuAction("deposit")}
                  disabled={!currentAccount}
                  className="w-full justify-start bg-blue-700 hover:bg-blue-800 text-white disabled:bg-blue-800 disabled:opacity-50"
                >
                  <PiggyBank className="w-4 h-4 mr-2" />
                  Make Deposit
                </Button>
                <Button
                  onClick={() => handleMenuAction("history")}
                  disabled={!currentAccount}
                  className="w-full justify-start bg-blue-700 hover:bg-blue-800 text-white disabled:bg-blue-800 disabled:opacity-50"
                >
                  <History className="w-4 h-4 mr-2" />
                  Transaction History
                </Button>
              </div>
            </div>

            {/* Card Reader */}
            <div className="bg-blue-800 p-4 rounded border-2 border-white">
              <div className="text-white text-center space-y-3">
                <div className="font-bold">CARD READER</div>
                <div className="text-sm">INSERT CARD</div>
                <Button
                  onClick={insertCard}
                  disabled={cardInserted}
                  className="w-full bg-blue-600 hover:bg-blue-700 text-white disabled:bg-gray-600"
                >
                  {cardInserted ? "Card Inserted" : "Insert Test Card"}
                </Button>
                <div className="text-xs text-blue-200">◯ Insert card to begin</div>
              </div>
            </div>
          </div>

          {/* Center Panel - Main Display */}
          <div className="flex-1 p-12">
            <Card className="h-96 bg-blue-900 border-4 border-gray-300">
              <CardContent className="h-full flex items-center justify-center p-8">{renderMainDisplay()}</CardContent>
            </Card>
          </div>

          {/* Right Panel - Keypad and Status */}
          <div className="w-80 p-6 space-y-6">
            {/* Keypad */}
            <Card className="bg-white">
              <CardContent className="p-4">
                <div className="text-center font-bold mb-4">Input Keypad</div>
                <div className="grid grid-cols-3 gap-2 mb-4">
                  {["1", "2", "3", "4", "5", "6", "7", "8", "9", "CLR", "0", "ENT"].map((key) => (
                    <Button
                      key={key}
                      onClick={() => handleKeypadInput(key)}
                      className="h-12 bg-gray-200 hover:bg-gray-300 text-black font-bold"
                    >
                      {key}
                    </Button>
                  ))}
                </div>
                <Button onClick={resetSession} className="w-full bg-red-600 hover:bg-red-700 text-white font-bold">
                  CANCEL TRANSACTION
                </Button>
              </CardContent>
            </Card>

            {/* System Status */}
            <Card className="bg-white">
              <CardContent className="p-4">
                <div className="text-center font-bold mb-4">System Status</div>
                <div className="space-y-2 text-sm">
                  <div className="flex justify-between">
                    <span>Connection:</span>
                    <Badge className="bg-green-500">Online</Badge>
                  </div>
                  <div className="flex justify-between">
                    <span>Card Status:</span>
                    <Badge className={cardInserted ? "bg-green-500" : "bg-gray-500"}>
                      {cardInserted ? "Inserted" : "Not Inserted"}
                    </Badge>
                  </div>
                  <div className="flex justify-between">
                    <span>Session:</span>
                    <Badge className={currentAccount ? "bg-green-500" : "bg-gray-500"}>
                      {currentAccount ? `Active - ${currentAccount.name}` : "Inactive"}
                    </Badge>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>

      {/* Notification Modal */}
      {notification.show && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <Card className="w-96 bg-white">
            <div
              className={`p-4 text-center text-white font-bold ${notification.success ? "bg-green-500" : "bg-red-500"}`}
            >
              {notification.success ? (
                <div className="flex items-center justify-center space-x-2">
                  <Check className="w-5 h-5" />
                  <span>SUCCESS</span>
                </div>
              ) : (
                <div className="flex items-center justify-center space-x-2">
                  <AlertCircle className="w-5 h-5" />
                  <span>ERROR</span>
                </div>
              )}
            </div>
            <CardContent className="p-6 text-center">
              <div className="font-bold text-lg mb-2">{notification.title}</div>
              <div className="text-gray-600 whitespace-pre-line">{notification.message}</div>
              <Button onClick={() => setNotification((prev) => ({ ...prev, show: false }))} className="mt-4">
                OK
              </Button>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}
