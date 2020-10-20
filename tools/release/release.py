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

        # Zip file
    if os.path.isdir(build_dir):
        if '-z' in sys.argv:
            print('Zipping...')
            shutil.make_archive(cqsp_dir + zip_name, 'zip', build_dir)

    print('Done!')
