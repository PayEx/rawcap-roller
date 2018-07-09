# RawCap Roller
This is a hacked-together command line utility, that allows
you to run RawCap.exe with rolling file functionality.

You can get RawCap.exe [here](http://www.netresec.com/?page=RawCap).

## Example of Use
Make sure you have the RawCap.exe file in the same directory, or specify it by adding `-rc <filepath>` or `--rawcap <filepath>`

### Do a test run
This will make it run 10 seconds each, on 3 files before stopping.
`java -jar rawcap-roller.jar --filecount 3 --seconds 10`

### Run it in the background
This will create one new file every hour, up to 12 times. Providing you a backlog of 12 hours of activity.

`javaw -jar rawcap-roller.jar --interface 1 --filecount 12 --seconds 3600 --roll --output "my-file.pcap"`

## Commands
###### -rc --rawcap
Full path to RawCap.exe. By default it looks in the same directory.

###### -o --output
Full path to the file you want to save. Default: dumpfile.pcap

###### -s --seconds --sec
Stop sniffing after <sec> seconds (per file). Default: Runs until manually stopped.

###### -i --interface
The interface number used by RawCap.exe. Run RawCap.exe manually to view your options. Default: 0.

###### -c -pc --packetcount
Stop sniffing after receiving <packetcount> packets (per file). Default: Runs until manually stopped.

###### -fc --filecount
Number of files to build (when using --seconds or --packetcount). Default: 1

###### -r --roll
Whether or not to roll indefinitely. When used with -c or -s, and --filecount, will allow you to keep running RawCap.exe without needing to stop it or worry about it ever-increasing in size.