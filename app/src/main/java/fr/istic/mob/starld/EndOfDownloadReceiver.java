package fr.istic.mob.starld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class EndOfDownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        byte [] buffer = new byte [1024];

        try {
            File dest = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dest, "Infos.zip");
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
            ZipEntry entry = zipInputStream.getNextEntry();


            while (entry != null) {
                System.out.println("Passer dans la boucle");
                String fileName = entry.getName();
                File newFile = new File(dest+File.separator+fileName);

                new File (newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int i;
                while ((i = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, i);
                }
                fos.close();
                entry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        }
        catch (IOException e) {
            System.out.println("Erreur : "+e);
        }
    }
}
