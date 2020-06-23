import os
import zipfile
import shutil
import sys

cqsp_dir = '../../'
build_dir = cqsp_dir + 'dist/'
zip_dir = cqsp_dir + 'Conquer-Space/'
zip_name = 'Conquer_Space'
if __name__ == '__main__':
    if os.path.isdir(build_dir):
        if '-z' in sys.argv:
            print('Zipping...')
            shutil.make_archive(cqsp_dir + zip_name, 'zip', build_dir)
            # Rename back so you don't lose it

        print('Done!')
    else:
        print('you probably need to recompile.')
