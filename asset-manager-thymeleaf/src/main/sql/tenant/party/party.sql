CREATE TABLE party(
  party_name         VARCHAR(64)  NOT NULL
 ,notes              TEXT
) INHERITS(base);

COMMENT ON TABLE party IS 'A party is a person or organization and is used as the interface to those entities.';

COMMENT ON COLUMN party.party_name IS 'The name to use for display purposes.';

SELECT ist_pk('party');
SELECT ist_bk('party', ARRAY['party_name']);
SELECT ist_org_index('party');
