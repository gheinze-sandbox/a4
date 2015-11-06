CREATE TABLE base(
  org_id                integer
 ,id                    bigint
 ,version               integer    DEFAULT 1
,inactive               boolean    DEFAULT false
);

COMMENT ON TABLE base IS 'Meta information of use for all tables in order to support auditing functions and concurrency support.  This table should never be inserted to directly. It is to serve as a parent table for inheritance.';

COMMENT ON COLUMN base.org_id IS 'Multi-tenanted support.';
COMMENT ON COLUMN base.id IS 'A unique identifier for records within a table.';
COMMENT ON COLUMN base.version IS 'The version of the record (typically equal to the number of times it was modified), used to address concurrency issues';




-- This table is only to be used for inheritence: don't allow direct input
CREATE RULE base_insert AS ON INSERT TO base DO INSTEAD NOTHING;


CREATE OR REPLACE FUNCTION trigger_base_update() RETURNS trigger AS $$
BEGIN
    new.version := old.version + 1;
    RETURN new;
END;
$$ LANGUAGE plpgsql;
