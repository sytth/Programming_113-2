# 讀取 CSV 檔案（路徑依你檔案位置調整）
Student_Main <- read.csv("C:/Users/Satellite/OneDrive/Programming_113-2/data/Student_Main.csv", header = TRUE, stringsAsFactors = FALSE)
Fees <- read.csv("C:/Users/Satellite/OneDrive/Programming_113-2/data/Fees.csv", header = TRUE, stringsAsFactors = FALSE)
Student_Payment <- read.csv("C:/Users/Satellite/OneDrive/Programming_113-2/data/Student_Payment.csv", header = TRUE, stringsAsFactors = FALSE)

# 用 merge 合併 Student_Main 和 Fees（左連接）
df <- merge(Student_Main, Student_Payment, by = "StudentID", all.x = TRUE)

# NA 補 0 (沒繳費視為 0)
df$AmountPaid[is.na(df$AmountPaid)] <- 0

# 再合併 Student_Payment 取得應繳金額
df <- merge(df, Fees, by = "PaymentType", all.x = TRUE)

# 重命名方便理解
names(df)[names(df) == "Amount"] <- "RequiredAmount"

df$RequiredAmount <- gsub(",", "", df$RequiredAmount)
df$AmountPaid <- gsub(",", "", df$AmountPaid)
df$RequiredAmount <- as.numeric(df$RequiredAmount)
df$AmountPaid <- as.numeric(df$AmountPaid)


print(df)

typeof(df$AmountPaid)
typeof(df$RequiredAmount)


# 計算 #1：繳費截止日前已繳總金額
total_received <- sum(df$AmountPaid, na.rm = TRUE)
cat("Computation #1: Total amount received before due =", total_received, "\n")

# 計算 #2：未繳足應繳費用者與差額
df_unpaid <- df[df$AmountPaid < df$RequiredAmount, ]
df_unpaid$AmountShort <- df_unpaid$RequiredAmount - df_unpaid$AmountPaid

# 列出結果
cat("Computation #2: Students who did not pay the required fees and amount short:\n")
print(df_unpaid[, c("StudentID", "Name", "PaymentType", "AmountPaid", "RequiredAmount", "AmountShort")])
