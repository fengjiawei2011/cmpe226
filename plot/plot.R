
# plot time
data = read.csv("~/Documents/EclipseWS/CMPE226_Project/plot/q3-time.csv", sep=",")
header = names(data)
colors = rainbow(3)

total = data[,1]
time1 = data[,2]

plot(total, time1, type= "n", main="Query3", xlab = "Total number of records", ylab = "Time used (sec)",bty='L', ylim=c(0,50))

colors = c(colors[1], colors[1], colors[3],colors[3])
linetype = c(1,2,1,2)
for (i in 2:5){
  print(header[i])
  lines(total, data[, i]/1000, type ="o", lty = linetype[i-1], col=colors[i-1], pch=16)
}

legend('topleft', legend = header[2:5], col = colors, lty=linetype, bty="n")

# plot cpu/mem
data = read.csv("~/Documents/EclipseWS/CMPE226_Project/plot/q3-cpu.csv", sep=",")
header = names(data)
colors = rainbow(3)

total = data[,1]
time1 = data[,2]

plot(total, time1, type= "n", main="Query3", xlab = "Total number of records", ylab = "CPU used (%)",bty='L', ylim=c(0,100), yaxs="i")

colors = c(colors[1], colors[1], colors[3],colors[3])
linetype = c(1,2,1,2)
for (i in 2:5){
  print(header[i])
  lines(total, data[, i], type ="o", lty = linetype[i-1], col=colors[i-1], pch=16)
}

legend('right', legend = header[2:5], col = colors, lty=linetype, bty="n")

# plot io
data = read.csv("~/Documents/EclipseWS/CMPE226_Project/plot/q3-io.csv", sep=",")
header = names(data)

total = data[,1]
time1 = data[,2]

plot(total, time1, type= "n", main="Query3", xlab = "Total number of records", ylab = "Transactions/sec",bty='L', ylim=c(0,15))

colors = c('red','red','blue', 'blue', 'orange','orange','green4','green4')
linetype = c(1,2,1,2,1,2,1,2)
for (i in 2:9){
  print(header[i])
  lines(total, data[, i], type ="o", lty = linetype[i-1], col=colors[i-1], pch=16)
}

legend('topleft', legend = header[2:5], col = colors[1:4], lty=linetype[1:4], bty="n")
legend('topright', legend = header[6:9], col = colors[5:8], lty=linetype[5:8], bty="n")
