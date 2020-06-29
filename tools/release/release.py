import os
import zipfile
import shutil
import sys
from sys import platform

cqsp_dir = '../../'
build_dir = cqsp_dir + 'dist/'
zip_dir = cqsp_dir + 'Conquer-Space/'
zip_name = 'Conquer_Space'
if __name__ == '__main__':
    if platform == "linux" or platform== "linux2" or platform == "darwin":
        #Nix platform 
        os.system('../proguard/bin/proguard.sh @{}proguard.txt')
    elif platform == "win32" or platform == "win64":
        # Windows
        os.system('proguard')
    # delete the original thing
    os.remove('{}Conquer-Space.jar'.format(build_dir))
    os.rename('{}Conquer-Space-compressed.jar'.format(build_dir), '{}Conquer-Space.jar'.format(build_dir))
    if os.path.isdir(build_dir):
        if '-z' in sys.argv:
            print('Zipping...')
            shutil.make_archive(cqsp_dir + zip_name, 'zip', build_dir)
            # Rename back so you don't lose it

        print('Done!')
    else:
        print('you probably need to recompile.')
