wmic Path win32_process Where "CommandLine Like '%%JOMI.jar%%'" Call Terminate
timeout 10