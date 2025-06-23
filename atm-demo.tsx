"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { ChevronRight, CreditCard, DollarSign, History, Key, LogOut, ArrowLeftRight, Eye } from "lucide-react"

type Screen =
  | "welcome"
  | "login"
  | "main-menu"
  | "balance"
  | "withdraw"
  | "deposit"
  | "transfer"
  | "history"
  | "change-pin"

export default function ATMDemo() {
  const [currentScreen, setCurrentScreen] = useState<Screen>("welcome")
  const [accountNumber, setAccountNumber] = useState("")
  const [pin, setPin] = useState("")
  const [amount, setAmount] = useState("")
  const [targetAccount, setTargetAccount] = useState("")
  const [currentUser] = useState("John Doe")
  const [balance] = useState(1500.0)

  const renderScreen = () => {
    switch (currentScreen) {
      case "welcome":
        return (
          <div className="text-center space-y-6">
            <div className="text-4xl font-bold text-blue-600 mb-4">🏧 SECURE ATM</div>
            <div className="border-2 border-gray-300 p-4 rounded">
              <h2 className="text-xl font-semibold mb-4">WELCOME TO SECURE ATM</h2>
              <div className="text-sm text-gray-600 mb-4">
                <p>Sample accounts for testing:</p>
                <p>Account: 1234567890, PIN: 1234 (John Doe - $1500.00)</p>
                <p>Account: 0987654321, PIN: 5678 (Jane Smith - $2500.00)</p>
                <p>Account: 1111222233, PIN: 9999 (Bob Johnson - $750.50)</p>
              </div>
              <Button onClick={() => setCurrentScreen("login")} className="w-full">
                Start Transaction
              </Button>
            </div>
          </div>
        )

      case "login":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- LOGIN ---</div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Account Number:</label>
                <Input
                  type="text"
                  value={accountNumber}
                  onChange={(e) => setAccountNumber(e.target.value)}
                  placeholder="Enter account number"
                  className="font-mono"
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">PIN:</label>
                <Input
                  type="password"
                  value={pin}
                  onChange={(e) => setPin(e.target.value)}
                  placeholder="Enter PIN"
                  maxLength={4}
                  className="font-mono"
                />
              </div>
              <div className="flex gap-2">
                <Button
                  onClick={() => setCurrentScreen("main-menu")}
                  className="flex-1"
                  disabled={!accountNumber || !pin}
                >
                  Login
                </Button>
                <Button variant="outline" onClick={() => setCurrentScreen("welcome")} className="bg-white text-black">
                  Back
                </Button>
              </div>
            </div>
          </div>
        )

      case "main-menu":
        return (
          <div className="space-y-4">
            <div className="text-center">
              <div className="text-lg font-semibold">Welcome, {currentUser}!</div>
              <div className="text-sm text-gray-600">Account: {accountNumber || "1234567890"}</div>
            </div>
            <div className="border-2 border-gray-300 p-4 rounded">
              <h2 className="text-xl font-semibold text-center mb-4">MAIN MENU</h2>
              <div className="grid gap-2">
                <Button
                  onClick={() => setCurrentScreen("balance")}
                  className="flex items-center justify-between p-4 h-auto"
                  variant="outline"
                >
                  <div className="flex items-center gap-2">
                    <Eye className="w-4 h-4" />
                    <span>1. Check Balance</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
                <Button
                  onClick={() => setCurrentScreen("withdraw")}
                  className="flex items-center justify-between p-4 h-auto"
                  variant="outline"
                >
                  <div className="flex items-center gap-2">
                    <DollarSign className="w-4 h-4" />
                    <span>2. Withdraw Cash</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
                <Button
                  onClick={() => setCurrentScreen("deposit")}
                  className="flex items-center justify-between p-4 h-auto"
                  variant="outline"
                >
                  <div className="flex items-center gap-2">
                    <CreditCard className="w-4 h-4" />
                    <span>3. Deposit Cash</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
                <Button
                  onClick={() => setCurrentScreen("transfer")}
                  className="flex items-center justify-between p-4 h-auto"
                  variant="outline"
                >
                  <div className="flex items-center gap-2">
                    <ArrowLeftRight className="w-4 h-4" />
                    <span>4. Transfer Money</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
                <Button
                  onClick={() => setCurrentScreen("history")}
                  className="flex items-center justify-between p-4 h-auto"
                  variant="outline"
                >
                  <div className="flex items-center gap-2">
                    <History className="w-4 h-4" />
                    <span>5. Transaction History</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
                <Button
                  onClick={() => setCurrentScreen("change-pin")}
                  className="flex items-center justify-between p-4 h-auto"
                  variant="outline"
                >
                  <div className="flex items-center gap-2">
                    <Key className="w-4 h-4" />
                    <span>6. Change PIN</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
                <Button
                  onClick={() => setCurrentScreen("login")}
                  className="flex items-center justify-between p-4 h-auto bg-red-600 hover:bg-red-700"
                >
                  <div className="flex items-center gap-2">
                    <LogOut className="w-4 h-4" />
                    <span>7. Logout</span>
                  </div>
                  <ChevronRight className="w-4 h-4" />
                </Button>
              </div>
            </div>
          </div>
        )

      case "balance":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- BALANCE INQUIRY ---</div>
            <div className="border-2 border-green-500 bg-green-50 p-6 rounded text-center">
              <div className="space-y-2">
                <p>
                  <strong>Account:</strong> {accountNumber || "1234567890"}
                </p>
                <p>
                  <strong>Account Holder:</strong> {currentUser}
                </p>
                <div className="text-2xl font-bold text-green-600 mt-4">Current Balance: ${balance.toFixed(2)}</div>
              </div>
            </div>
            <Button onClick={() => setCurrentScreen("main-menu")} className="w-full" variant="outline">
              Back to Main Menu
            </Button>
          </div>
        )

      case "withdraw":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- CASH WITHDRAWAL ---</div>
            <div className="bg-blue-50 p-4 rounded">
              <p>
                <strong>Current Balance:</strong> ${balance.toFixed(2)}
              </p>
              <p className="text-sm text-gray-600 mt-2">Available denominations: $20, $50, $100</p>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Withdrawal Amount:</label>
              <div className="flex">
                <span className="inline-flex items-center px-3 text-sm text-gray-900 bg-gray-200 border border-r-0 border-gray-300 rounded-l-md">
                  $
                </span>
                <Input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="Enter amount"
                  className="rounded-l-none"
                />
              </div>
            </div>
            <div className="grid grid-cols-3 gap-2">
              <Button onClick={() => setAmount("20")} variant="outline" className="bg-white text-black">
                $20
              </Button>
              <Button onClick={() => setAmount("50")} variant="outline" className="bg-white text-black">
                $50
              </Button>
              <Button onClick={() => setAmount("100")} variant="outline" className="bg-white text-black">
                $100
              </Button>
            </div>
            <div className="flex gap-2">
              <Button className="flex-1" disabled={!amount}>
                Withdraw
              </Button>
              <Button variant="outline" onClick={() => setCurrentScreen("main-menu")} className="bg-white text-black">
                Cancel
              </Button>
            </div>
          </div>
        )

      case "deposit":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- CASH DEPOSIT ---</div>
            <div className="bg-green-50 p-4 rounded">
              <p>
                <strong>Current Balance:</strong> ${balance.toFixed(2)}
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Deposit Amount:</label>
              <div className="flex">
                <span className="inline-flex items-center px-3 text-sm text-gray-900 bg-gray-200 border border-r-0 border-gray-300 rounded-l-md">
                  $
                </span>
                <Input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="Enter amount"
                  className="rounded-l-none"
                />
              </div>
            </div>
            <div className="flex gap-2">
              <Button className="flex-1" disabled={!amount}>
                Deposit
              </Button>
              <Button variant="outline" onClick={() => setCurrentScreen("main-menu")} className="bg-white text-black">
                Cancel
              </Button>
            </div>
          </div>
        )

      case "transfer":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- MONEY TRANSFER ---</div>
            <div className="bg-blue-50 p-4 rounded">
              <p>
                <strong>Current Balance:</strong> ${balance.toFixed(2)}
              </p>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Target Account Number:</label>
              <Input
                type="text"
                value={targetAccount}
                onChange={(e) => setTargetAccount(e.target.value)}
                placeholder="Enter target account number"
                className="font-mono"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Transfer Amount:</label>
              <div className="flex">
                <span className="inline-flex items-center px-3 text-sm text-gray-900 bg-gray-200 border border-r-0 border-gray-300 rounded-l-md">
                  $
                </span>
                <Input
                  type="number"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="Enter amount"
                  className="rounded-l-none"
                />
              </div>
            </div>
            <div className="flex gap-2">
              <Button className="flex-1" disabled={!targetAccount || !amount}>
                Transfer
              </Button>
              <Button variant="outline" onClick={() => setCurrentScreen("main-menu")} className="bg-white text-black">
                Cancel
              </Button>
            </div>
          </div>
        )

      case "history":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- TRANSACTION HISTORY ---</div>
            <div className="bg-gray-50 p-4 rounded">
              <div className="text-xs font-mono">
                <div className="border-b pb-2 mb-2 font-bold">TYPE | AMOUNT | BALANCE | DATE & TIME | DESCRIPTION</div>
                <div className="space-y-1 text-xs">
                  <div>DEPOSIT | $1500.00 | $1500.00 | 2024-01-15 09:00:00 | Initial deposit</div>
                  <div>WITHDRAWAL | $ 100.00 | $1400.00 | 2024-01-15 14:30:00 | Cash withdrawal</div>
                  <div>DEPOSIT | $ 200.00 | $1600.00 | 2024-01-16 10:15:00 | Cash deposit</div>
                  <div>TRANSFER_OUT | $ 100.00 | $1500.00 | 2024-01-16 16:45:00 | Transfer to 0987654321</div>
                </div>
              </div>
            </div>
            <Button onClick={() => setCurrentScreen("main-menu")} className="w-full" variant="outline">
              Back to Main Menu
            </Button>
          </div>
        )

      case "change-pin":
        return (
          <div className="space-y-4">
            <div className="text-center text-xl font-semibold mb-6">--- CHANGE PIN ---</div>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Current PIN:</label>
                <Input type="password" placeholder="Enter current PIN" maxLength={4} className="font-mono" />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">New PIN (4 digits):</label>
                <Input type="password" placeholder="Enter new PIN" maxLength={4} className="font-mono" />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Confirm New PIN:</label>
                <Input type="password" placeholder="Confirm new PIN" maxLength={4} className="font-mono" />
              </div>
              <div className="flex gap-2">
                <Button className="flex-1">Change PIN</Button>
                <Button variant="outline" onClick={() => setCurrentScreen("main-menu")} className="bg-white text-black">
                  Cancel
                </Button>
              </div>
            </div>
          </div>
        )

      default:
        return null
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-100 to-blue-200 p-4">
      <div className="max-w-md mx-auto">
        <Card className="bg-white shadow-2xl border-4 border-gray-800">
          <CardHeader className="bg-gray-800 text-white text-center">
            <CardTitle className="text-lg font-bold">ATM SIMULATOR</CardTitle>
            <div className="text-sm opacity-75">Interactive Demo</div>
          </CardHeader>
          <CardContent className="p-6 min-h-[500px]">{renderScreen()}</CardContent>
        </Card>

        <div className="mt-4 text-center text-sm text-gray-600">
          <p>This is a visual representation of the console-based ATM interface</p>
          <p>Click through the different screens to see how it works</p>
        </div>
      </div>
    </div>
  )
}
