data = read.csv("~/Documents/EclipseWS/CMPE226_Project/plot/data.csv", sep=",")
header = names(data)
colors = rainbow(4)
linetype = c(1:1)

total = data[,1]
time1 = data[,2]

plot(total, time1, type= "n", xlab = "total number of records", ylab = "time used")


for (i in 2:5){
  print(header[i])
  lines(total, data[, i], type ="o", lty = linetype[1], col=colors[i-1], pch=16)
}

legend('right', legend = header[2:5], col = colors, lty=linetype)
