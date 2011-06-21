SET ECLIPSE="C:\Program Files\eclipse-platform-3.6.2-win32\eclipse\eclipse.exe"
IF EXIST %ECLIPSE% (%ECLIPSE% -data . ; goto exit)
SET ECLIPSE="C:\Program Files\pleiades-e3.6-platform-jre_20100623\eclipse\eclipse.exe"
IF EXIST %ECLIPSE% (%ECLIPSE% -data . ; goto exit)
SET ECLIPSE="C:\Program Files (x86)\pleiades-e3.6-ultimate-jre_20101005\eclipse\eclipse.exe"
IF EXIST %ECLIPSE% (%ECLIPSE% -data . ; goto exit)
SET ECLIPSE="C:\Program Files (x86)\pleiades-e3.6-platform-jre_20101025\eclipse\eclipse.exe"
IF EXIST %ECLIPSE% (%ECLIPSE% -data . ; goto exit)
SET ECLIPSE="C:\Program Files\pleiades-e3.6-platform-jre_20101025\eclipse\eclipse.exe"
IF EXIST %ECLIPSE% (%ECLIPSE% -data . ; goto exit)
:exit
