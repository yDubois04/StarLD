package fr.istic.mob.starld;

public class LaStarAPIResult {
    int nhits;
    LaStarParameters parameters;
    LaStarRecords[] records;


    public class LaStarParameters{
        String dataset, timezone, format;
        int rows;
    }

    public class LaStarRecords{
        String datasetid, recordid, record_timestamp;
        LaStarRecordsFields fields;

        public class LaStarRecordsFields{
            LaStarRecordsFieldsFichier fichier;
            String description, url, debutvalidite, finvalidite, id, publication;
            int tailleoctets;

            public class LaStarRecordsFieldsFichier{
                String mimetype, format, filename, id;
                int width, height;
                boolean thumbnail;


            }
        }
    }
}
