#Gnuplot script to generate diagrams from ab's output
#Thanks to http://tjholowaychuk.com/post/543349452/apachebench-gnuplot-graphing-benchmarks

# output as png image
set terminal png

# nicer aspect ratio for image size
# set size 1,0.7

# y-axis grid
set grid y
set xlabel "request" 
set ylabel "response time (ms)"

   
set output "time10.png"
set title "10 concurrent requests"
set yrange [0:120]
plot "~/VirtualBox VMs/shared/servlet2.5-c10" using 9 smooth sbezier with lines title "servlet 2.5 -c 10", \
        "~/VirtualBox VMs/shared/servlet3.0-c10" using 9 smooth sbezier with lines title "servlet 3.0 -c 10", \
	 "~/VirtualBox VMs/shared/grizzly-c10"   using 9 smooth sbezier with lines title "grizzly -c 10"

set output "time100.png"
	    set yrange [0:400]
	    set title "100 concurrent requests"

plot "~/VirtualBox VMs/shared/servlet2.5-c100" using 9 smooth sbezier with lines title "servlet 2.5 -c 100", \
        "~/VirtualBox VMs/shared/servlet3.0-c100" using 9 smooth sbezier with lines title "servlet 3.0 -c 100", \
	 "~/VirtualBox VMs/shared/grizzly-c100"   using 9 smooth sbezier with lines title "grizzly -c 100"

set output "time200.png"
set yrange [0:600]
	    set title "200 concurrent requests"

   plot	 "~/VirtualBox VMs/shared/servlet2.5-c200" using 9 smooth sbezier with lines title "servlet 2.5 -c 200", \
         "~/VirtualBox VMs/shared/servlet3.0-c200"  using 9 smooth sbezier with lines title "servlet 3.0 -c 200", \
	 "~/VirtualBox VMs/shared/grizzly-c200"    using 9 smooth sbezier with lines title "grizzly -c 200"

set output "time500.png"
set yrange [0:2000]
	    set title "500 concurrent requests"

   plot	 "~/VirtualBox VMs/shared/servlet2.5-c500" using 9 smooth sbezier with lines title "servlet 2.5 -c 500", \
         "~/VirtualBox VMs/shared/servlet3.0-c500"  using 9 smooth sbezier with lines title "servlet 3.0 -c 500", \
	 "~/VirtualBox VMs/shared/grizzly-c500"    using 9 smooth sbezier with lines title "grizzly -c 500"

set output "time1000.png"
set yrange [0:4000]
	    set title "1000 concurrent requests"

   plot	 "~/VirtualBox VMs/shared/servlet2.5-c1000" using 9 smooth sbezier with lines title "servlet 2.5 -c 1000", \
         "~/VirtualBox VMs/shared/servlet3.0-c1000"  using 9 smooth sbezier with lines title "servlet 3.0 -c 1000", \
	 "~/VirtualBox VMs/shared/grizzly-c1000"    using 9 smooth sbezier with lines title "grizzly -c 1000"
	    