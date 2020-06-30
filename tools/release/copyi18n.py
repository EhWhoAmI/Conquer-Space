def copy_i18n(cqsp_dir):
    src_dir = cqsp_dir + 'src/'
    i18n_file = src_dir + 'assets/i18n/ApplicationMessages_en_US.properties'
    i18n_output = cqsp_dir + 'assets/i18n/EmptyLocale.properties'
    with open(i18n_file, 'r') as f:
        Lines = f.readlines() 
        with open(i18n_output, 'w') as output:
            # Strips the newline character
            for line in Lines: 
                # Skip first line
                if(line.startswith('# Default')):
                    output.write('# To add your own locale, copy this file and add your own locale\n')
                    output.write('# Then rename it Application_<lang>_<country>.properties\n')
                elif(line.startswith('#')):
                    # Comment
                    output.write(line)
                    pass
                elif line.strip():
                    keyset = line.split('=')
                    output.write((keyset[0] + '='))
                    output.write('\n')
                else:
                    output.write('\n')

if __name__ == '__main__':
    cqsp_dir = '../../'
    copy_i18n(cqsp_dir)