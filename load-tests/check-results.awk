# Read CSV records, including quoted commas and multiline assertion messages.
function checkRecord(    column) {
    if (!headerRead) {
        for (column = 1; column <= fieldCount; column++) {
            if (fields[column] == "success") successColumn = column
        }
        headerRead = 1
        return
    }
    if (fields[successColumn] != "true" && fields[successColumn] != "false") {
        invalid = 1
        return
    }
    total++
    if (fields[successColumn] == "false") failed++
}
{
    sub(/\r$/, "")
    for (position = 1; position <= length($0); position++) {
        character = substr($0, position, 1)
        if (character == "\"") {
            if (quoted && substr($0, position + 1, 1) == "\"") {
                field = field "\""
                position++
            } else {
                quoted = !quoted
            }
        } else if (character == "," && !quoted) {
            fields[++fieldCount] = field
            field = ""
        } else {
            field = field character
        }
    }
    if (quoted) {
        field = field "\n"
        next
    }
    fields[++fieldCount] = field
    checkRecord()
    delete fields
    fieldCount = 0
    field = ""
}
END {
    if (quoted || invalid || !successColumn || !total) {
        print "Cannot validate JMeter results: empty or invalid CSV." > "/dev/stderr"
        exit 2
    }
    printf "Samples: %d, failed: %d\n", total, failed
    exit(failed > 0)
}
