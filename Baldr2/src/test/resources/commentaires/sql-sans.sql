
SELECT nom, service 
FROM   employe 
WHERE  statut = 'stagiaire' 
ORDER  BY nom; 

DELETE FROM a_table
    WHERE field2 = 'N';
	
INSERT INTO a_table (field1, field2, field3)
    VALUES ('test', 'N', NULL);