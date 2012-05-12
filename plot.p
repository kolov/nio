#Thanks to http://tjholowaychuk.com/post/543349452/apachebench-gnuplot-graphing-benchmarks

# output as png image
set terminal png

# save file to "out.png"
set output "out.png"

# graph title
set title "Comparison long running requests"

# nicer aspect ratio for image size
set size 1,0.7

# y-axis grid
set grid y

# x-axis label
set xlabel "request"
   set yrange [0:600]

# y-axis label
set ylabel "response time (ms)"

# plot data from "out.dat" using column 9 with smooth sbezier lines
# and title of "nodejs" for the given data
   plot "~/ubuntu/servlet2.5-c100" using 9 smooth sbezier with lines title "servlet 2.5 -c 100", \
        "~/ubuntu/servlet3.0-c100" using 9 smooth sbezier with lines title "servlet 3.0 -c 100", \
	 "~/ubuntu/grizzly-c100"   using 9 smooth sbezier with lines title "grizzly -c 100", \
	 "~/ubuntu/servlet2.5-c200" using 9 smooth sbezier with lines title "servlet 2.5 -c 200", \
        "~/ubuntu/servlet3.0-c200"  using 9 smooth sbezier with lines title "servlet 3.0 -c 200", \
	 "~/ubuntu/grizzly-c200"    using 9 smooth sbezier with lines title "grizzly -c 200"
	    