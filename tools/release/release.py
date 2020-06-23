import os
import zipfile
import shutil
import platform
cqsp_dir = '../../'
build_dir = cqsp_dir + 'dist/'
zip_dir = cqsp_dir + 'Conquer-Space/'
zip_name = 'Conquer_Space'
if __name__ == '__main__':
    if os.path.isdir(build_dir):
        # Rename files
        # Remove og conquer space file
        os.remove(build_dir + 'Conquer-Space.jar')
        #os.rename(build_dir + 'Conquer-Space-compressed.jar', build_dir + 'Conquer-Space.jar')
        os.rename(build_dir, zip_dir)
        print('Zipping...')
        shutil.make_archive(cqsp_dir + zip_name, 'zip', zip_dir)
        # Rename back so you don't lose it
        os.rename(zip_dir, build_dir)
        print('Done!')
    else:
        print('you probably need to recompile.')
