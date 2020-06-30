import os
import zipfile
import shutil
import sys
from sys import platform
import copyi18n

cqsp_dir = '../../'
build_dir = cqsp_dir + 'dist/'
zip_dir = cqsp_dir + 'Conquer-Space/'
zip_name = 'Conquer_Space'
proguard_file = '../../proguard.txt'
if __name__ == '__main__':
    # Pre compress
    # Copy the empty translations
    copyi18n.copy_i18n(cqsp_dir)

    # -f allows you to specify the proguard file
    if '-f' in sys.argv:
        ind = sys.argv.index('-f')
        proguard_file = sys.argv[ind+1]
    print(proguard_file)
    # Run proguard
    success = -1
    if platform == "linux" or platform== "linux2" or platform == "darwin":
        #Nix platform 
        success = os.system('../proguard/bin/proguard.sh @../../proguard.txt')
    elif platform == "win32" or platform == "win64":
        # Windows
        success = os.system(('proguard ' + proguard_file))

    # Proguard succeeded
    if(success == 0):
        # delete the original file
        os.remove('{}Conquer-Space.jar'.format(build_dir))
        os.rename('{}Conquer-Space-compressed.jar'.format(build_dir), '{}Conquer-Space.jar'.format(build_dir))
        
        # Zip file
        if os.path.isdir(build_dir):
            if '-z' in sys.argv:
                print('Zipping...')
                shutil.make_archive(cqsp_dir + zip_name, 'zip', build_dir)

        print('Done!')
    else:
        print('Proguard failed!')