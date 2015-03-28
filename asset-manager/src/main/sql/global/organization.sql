CREATE TABLE organization(

   id            serial
  ,org_name      character varying(64) NOT NULL
  ,email_address character varying(64)
  
  ,CONSTRAINT organization_pk PRIMARY KEY (id)
  ,CONSTRAINT organization_name_uk UNIQUE (org_name)

);
